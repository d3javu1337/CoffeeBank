package jwt

import (
	"AuthMS/config"
	"AuthMS/model/principal"
	"log/slog"
	"time"

	"github.com/golang-jwt/jwt/v5"
)

type Tokens struct {
	AccessToken  string
	RefreshToken string
}

type CustomClaims struct {
	Roles []principal.Role `json:"rls,omitempty"`
	jwt.RegisteredClaims
}

type TokenType string

const (
	ACCESS  TokenType = "ACCESS"
	REFRESH TokenType = "REFRESH"
)

type JWTService interface {
	GenerateAccessToken(pr *principal.Principal) (string, error)
	GenerateRefreshToken(pr *principal.Principal) (string, error)
	GenerateBothTokens(pr *principal.Principal) (*Tokens, error)
	GetEmail(token string, tokenType TokenType) (string, error)
	GetExpiration(token string, tokenType TokenType) (*time.Time, error)
	GetIssuedAt(token string, tokenType TokenType) (*time.Time, error)
	GetRoles(token string, tokenType TokenType) ([]principal.Role, error)
}

type JWTServiceImpl struct {
	config *config.JWTConfig
}

func NewJWTServiceImpl(cfg *config.JWTConfig) *JWTServiceImpl {
	return &JWTServiceImpl{config: cfg}
}

func (service *JWTServiceImpl) GenerateAccessToken(pr *principal.Principal) (string, error) {
	now := time.Now()
	exp := now.Add(time.Minute * 15)
	return service.generateAccessToken(pr, now, exp)
}

func (service *JWTServiceImpl) generateAccessToken(pr *principal.Principal, now time.Time, exp time.Time) (string, error) {
	claims := CustomClaims{
		Roles: pr.Roles(),
		RegisteredClaims: jwt.RegisteredClaims{
			Subject:   pr.Email(),
			ExpiresAt: jwt.NewNumericDate(exp),
			IssuedAt:  jwt.NewNumericDate(now),
		},
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	stringToken, err := token.SignedString([]byte(service.config.AccessSecret))
	if err != nil {
		slog.Error("Error when signing jwt", "error", err)
		return "", err
	}
	return stringToken, nil
}

func (service *JWTServiceImpl) GenerateRefreshToken(pr *principal.Principal) (string, error) {
	now := time.Now()
	exp := now.AddDate(0, 1, 0)
	return service.generateRefreshToken(pr, now, exp)
}

func (service *JWTServiceImpl) generateRefreshToken(pr *principal.Principal, now time.Time, exp time.Time) (string, error) {
	claims := CustomClaims{
		Roles: pr.Roles(),
		RegisteredClaims: jwt.RegisteredClaims{
			Subject:   pr.Email(),
			ExpiresAt: jwt.NewNumericDate(exp),
			IssuedAt:  jwt.NewNumericDate(now),
		},
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	stringToken, err := token.SignedString([]byte(service.config.RefreshSecret))
	if err != nil {
		slog.Error("Error when signing jwt", "error", err)
		return "", err
	}
	return stringToken, nil
}

func (service *JWTServiceImpl) GenerateBothTokens(pr *principal.Principal) (*Tokens, error) {
	access, err := service.GenerateAccessToken(pr)
	if err != nil {
		return nil, err
	}
	refresh, err := service.GenerateRefreshToken(pr)
	if err != nil {
		return nil, err
	}
	return &Tokens{
		AccessToken:  access,
		RefreshToken: refresh,
	}, nil
}

func (service *JWTServiceImpl) GetEmail(token string, tokenType TokenType) (string, error) {
	parsed, err := jwt.ParseWithClaims(token, &CustomClaims{}, func(token *jwt.Token) (any, error) {
		return []byte(service.getSecret(tokenType)), nil
	})
	if err != nil {
		slog.Error("Token parse error", "error", err)
		return "", err
	}
	subject, err := parsed.Claims.GetSubject()
	if err != nil {
		return "", err
	}
	return subject, nil
}

func (service *JWTServiceImpl) GetExpiration(token string, tokenType TokenType) (*time.Time, error) {
	parsed, err := jwt.ParseWithClaims(token, &CustomClaims{}, func(token *jwt.Token) (any, error) {
		return []byte(service.getSecret(tokenType)), nil
	})
	if err != nil {
		slog.Error("Token parse error", "error", err)
		return nil, err
	}
	expiration, err := parsed.Claims.GetExpirationTime()
	if err != nil {
		return nil, err
	}
	return &expiration.Time, nil
}

func (service *JWTServiceImpl) GetIssuedAt(token string, tokenType TokenType) (*time.Time, error) {
	parsed, err := jwt.ParseWithClaims(token, &CustomClaims{}, func(token *jwt.Token) (any, error) {
		return []byte(service.getSecret(tokenType)), nil
	})
	if err != nil {
		slog.Error("Token parse error", "error", err)
		return nil, err
	}
	expiration, err := parsed.Claims.GetIssuedAt()
	if err != nil {
		return nil, err
	}
	return &expiration.Time, nil
}

func (service *JWTServiceImpl) GetRoles(token string, tokenType TokenType) ([]principal.Role, error) {
	parsed, err := jwt.ParseWithClaims(token, &CustomClaims{}, func(token *jwt.Token) (any, error) {
		return []byte(service.getSecret(tokenType)), nil
	})
	if err != nil {
		slog.Error("Token parse error", "error", err)
		return nil, err
	}
	v, ok := parsed.Claims.(*CustomClaims)
	if !ok {
		slog.Error("Token parse error", "error", err)
		return nil, err
	}
	roles := v.Roles
	return roles, nil
}

func (service *JWTServiceImpl) getSecret(tokenType TokenType) string {
	if tokenType == ACCESS {
		return service.config.AccessSecret
	}
	return service.config.RefreshSecret
}
