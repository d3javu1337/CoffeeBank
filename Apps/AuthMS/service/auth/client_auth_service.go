package auth

import (
	routesDto "AuthMS/handlers/dto"
	"AuthMS/model/auth_data"
	"AuthMS/model/base_client"
	"AuthMS/model/business_client"
	"AuthMS/model/errors"
	"AuthMS/model/outbox"
	"AuthMS/model/principal"
	"AuthMS/repositry/postgres"
	"AuthMS/service/jwt"
	"AuthMS/service/outbox/dto"
	"AuthMS/service/security"
	"AuthMS/service/session"
	"context"
	"encoding/json"
	"fmt"
	"log/slog"

	"github.com/jackc/pgx/v5"
)

type ClientAuthService interface {
	BaseRegistration(ctx context.Context, request *base_client.BaseRegistrationRequest) error
	BusinessRegistration(ctx context.Context, request *business_client.BusinessRegistrationRequest) error
	Login(ctx context.Context, dto routesDto.LoginDto, userAgent string) (*jwt.Tokens, error)
	Refresh(ctx context.Context, refreshToken string) (*jwt.Tokens, error)
	Logout(ctx context.Context, accessToken string) error
}

type ClientAuthServiceImpl struct {
	authRepo        postgres.ClientAuthRepository
	outboxRepo      postgres.OutboxRepository
	sessionService  session.SessionService
	securityService security.SecurityService
	jwtService      jwt.JWTService
}

func NewClientAuthServiceImpl(
	authRepo postgres.ClientAuthRepository,
	outboxRepo postgres.OutboxRepository,
	sessionService session.SessionService,
	securityService security.SecurityService,
	jwtService jwt.JWTService,
) *ClientAuthServiceImpl {
	return &ClientAuthServiceImpl{
		authRepo:        authRepo,
		outboxRepo:      outboxRepo,
		sessionService:  sessionService,
		securityService: securityService,
		jwtService:      jwtService,
	}
}

func (service *ClientAuthServiceImpl) BaseRegistration(ctx context.Context, request *base_client.BaseRegistrationRequest) error {
	exists, err := service.authRepo.ExistsClientByEmail(ctx, request.Email)
	if err != nil {
		slog.Error("Error while db query: ", err)
		return err
	}
	if *exists {
		return errors.NewClientAlreadyExistsError(request.Email)
	}

	hash, err := service.securityService.HashPassword(request.Password)
	if err != nil {
		return err
	}

	transaction, id, err := service.authRepo.InsertClientAuthData(ctx, auth_data.AuthData{
		Email:        request.Email,
		PhoneNumber:  &request.PhoneNumber,
		PasswordHash: *hash,
	})
	if err != nil {
		return err
	}
	defer func(transaction pgx.Tx, ctx context.Context) {
		_ = transaction.Rollback(ctx)
	}(transaction, ctx)

	serializedDto, err := json.Marshal(dto.BaseRegistrationDto{
		Surname:     request.Surname,
		Name:        request.Name,
		Patronymic:  request.Patronymic,
		DateOfBirth: request.DateOfBirth,
		PhoneNumber: request.PhoneNumber,
		Email:       request.Email,
	})
	if err != nil {
		slog.Error("Json marshaling error", "error", err)
		return err
	}

	err = service.outboxRepo.InsertRegistrationRecord(ctx, transaction, *id, serializedDto, outbox.BASE)
	if err != nil {
		slog.Error("Outbox insert error", "error", err)
		return err
	}

	if err := transaction.Commit(ctx); err != nil {
		slog.Error("Error while commiting transaction: ", err)
		return err
	}

	return nil
}

func (service *ClientAuthServiceImpl) BusinessRegistration(ctx context.Context, request *business_client.BusinessRegistrationRequest) error {
	exists, err := service.authRepo.ExistsClientByEmail(ctx, request.Email)
	if err != nil {
		slog.Error("Error while db query: ", err)
		return err
	}
	if *exists {
		return errors.NewClientAlreadyExistsError(request.Email)
	}

	hash, err := service.securityService.HashPassword(request.Password)
	if err != nil {
		return err
	}

	transaction, id, err := service.authRepo.InsertClientAuthData(ctx, auth_data.AuthData{
		Email:        request.Email,
		PhoneNumber:  nil,
		PasswordHash: *hash,
	})
	if err != nil {
		return err
	}
	defer func(transaction pgx.Tx, ctx context.Context) {
		_ = transaction.Rollback(ctx)
	}(transaction, ctx)

	serializedDto, err := json.Marshal(dto.BusinessRegistrationDto{
		OfficialName: request.OfficialName,
		Brand:        request.Brand,
		Email:        request.Email,
	})
	if err != nil {
		slog.Error("Json marshaling error", "error", err)
		return err
	}

	err = service.outboxRepo.InsertRegistrationRecord(ctx, transaction, *id, serializedDto, outbox.BUSINESS)
	if err != nil {
		slog.Error("Outbox insert error", "error", err)
		return err
	}

	if err := transaction.Commit(ctx); err != nil {
		slog.Error("Error while commiting transaction: ", err)
		return err
	}

	return nil
}

func (service *ClientAuthServiceImpl) Login(ctx context.Context, dto routesDto.LoginDto, userAgent string) (*jwt.Tokens, error) {
	data, err := service.authRepo.LoadAuthData(ctx, dto.Email)
	if err != nil {
		slog.Error("Login error", "error", err)
		return nil, err
	}
	err = service.securityService.VerifyPassword(dto.Password, data.PasswordHash)
	if err != nil {
		slog.Error("Login error. Wrong credentials")
	}
	clientId, err := service.authRepo.FindClientIdByEmail(ctx, dto.Email)
	if err != nil {
		return nil, err
	}
	roles, err := service.authRepo.LoadRoles(ctx, *clientId)
	pr := principal.NewPrincipal(dto.Email, roles)
	tokens, err := service.jwtService.GenerateBothTokens(&pr)
	service.sessionService.RegisterSession(ctx, dto.Email, userAgent, tokens.AccessToken, tokens.RefreshToken)
	return tokens, nil
}

func (service *ClientAuthServiceImpl) Refresh(ctx context.Context, refreshToken string) (*jwt.Tokens, error) {
	email, err := service.jwtService.GetEmail(refreshToken, jwt.REFRESH)
	if err != nil {
		return nil, err
	}
	clientId, err := service.authRepo.FindClientIdByEmail(ctx, email)
	if err != nil {
		return nil, err
	}
	roles, err := service.authRepo.LoadRoles(ctx, *clientId)
	if err != nil {
		return nil, err
	}
	pr := principal.NewPrincipal(email, roles)
	tokens, err := service.jwtService.GenerateBothTokens(&pr)
	if err != nil {
		return nil, err
	}
	res, err := service.sessionService.UpdateSessionOnTokenRefresh(ctx, email, refreshToken, tokens.AccessToken, tokens.RefreshToken)
	if err != nil {
		return nil, err
	}
	if !res {
		return nil, fmt.Errorf("session update was not acknowledged")
	}
	return tokens, nil
}

func (service *ClientAuthServiceImpl) Logout(ctx context.Context, accessToken string) error {
	email, err := service.jwtService.GetEmail(accessToken, jwt.ACCESS)
	if err != nil {
		return err
	}
	res, err := service.sessionService.Logout(ctx, email, accessToken)
	if err != nil {
		return err
	}
	if !res {
		return fmt.Errorf("session close by access token was not acknowledged")
	}
	return nil
}
