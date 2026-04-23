package handlers

import (
	"AuthMS/handlers/dto"
	"AuthMS/model/base_client"
	"AuthMS/model/business_client"
	"AuthMS/service/auth"
	"encoding/json"
	"fmt"
	"io"
	"log/slog"
	"net/http"
	"time"

	"github.com/go-playground/validator/v10"
)

type AuthHandler interface {
	BaseRegistration(w http.ResponseWriter, r *http.Request)
	BusinessRegistration(w http.ResponseWriter, r *http.Request)
	Login(w http.ResponseWriter, r *http.Request)
	Logout(w http.ResponseWriter, r *http.Request)
	Refresh(w http.ResponseWriter, r *http.Request)
}

const refreshTokenCookieName = "refreshToken"

// Requires ref as v param. Writes error in ResponseWriter by self.
//
// Requires defer http.Request.Body close
func parseBodyWithJsonValidation[T interface{}](w http.ResponseWriter, r *http.Request, v T) error {
	rawBody, err := io.ReadAll(r.Body)

	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return err
	}

	err = json.Unmarshal(rawBody, &v)
	if err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return err
	}

	err = validate.Struct(v)
	if err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return err
	}
	return nil
}

func writeRefreshTokenInHttpCookie(w http.ResponseWriter, refreshToken string) {
	http.SetCookie(w, &http.Cookie{
		Name:     refreshTokenCookieName,
		Value:    refreshToken,
		Quoted:   false,
		Path:     "***/***",
		Expires:  time.Time{},
		MaxAge:   30 * 24 * 60 * 60,
		HttpOnly: true,
	})
}

type AuthHandlerImpl struct {
	authService auth.ClientAuthService
}

var validate = validator.New()

func NewAuthHandlerImpl(authService auth.ClientAuthService) *AuthHandlerImpl {
	return &AuthHandlerImpl{
		authService: authService,
	}
}

func (handler *AuthHandlerImpl) BaseRegistration(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()

	var parsed dto.BaseRegistrationDto

	err := parseBodyWithJsonValidation(w, r, &parsed)
	if err != nil {
		return
	}

	err = handler.authService.BaseRegistration(r.Context(), &base_client.BaseRegistrationRequest{
		Surname:     parsed.Surname,
		Name:        parsed.Name,
		Patronymic:  parsed.Patronymic,
		DateOfBirth: parsed.DateOfBirth,
		PhoneNumber: parsed.PhoneNumber,
		Email:       parsed.Email,
		Password:    parsed.Password,
	})
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	w.WriteHeader(http.StatusAccepted)
}

func (handler *AuthHandlerImpl) BusinessRegistration(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()

	var parsed dto.BusinessRegistrationDto

	err := parseBodyWithJsonValidation(w, r, &parsed)
	if err != nil {
		return
	}

	err = handler.authService.BusinessRegistration(r.Context(), &business_client.BusinessRegistrationRequest{
		OfficialName: parsed.OfficialName,
		Brand:        parsed.Brand,
		Email:        parsed.Email,
		Password:     parsed.Password,
	})
	if err != nil {
		slog.Error("error", "err", err)
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	w.WriteHeader(http.StatusAccepted)
}

func (handler *AuthHandlerImpl) Login(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()

	var loginDto dto.LoginDto
	err := parseBodyWithJsonValidation(w, r, &loginDto)
	if err != nil {
		return
	}

	tokens, err := handler.authService.Login(r.Context(), loginDto, r.UserAgent())
	if err != nil {
		http.Error(w, fmt.Sprintf("Error occurred when login: %s", err.Error()), http.StatusInternalServerError)
		return
	}
	writeRefreshTokenInHttpCookie(w, tokens.RefreshToken)
	w.WriteHeader(http.StatusOK)
	_, err = io.WriteString(w, tokens.AccessToken)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
}

func (handler *AuthHandlerImpl) Logout(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()
	err := handler.authService.Logout(r.Context(), r.PathValue(AccessTokenPathParamName))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	w.WriteHeader(http.StatusOK)
}

func (handler *AuthHandlerImpl) Refresh(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()
	refreshCookie, err := r.Cookie(refreshTokenCookieName)
	if err != nil {
		http.Error(w, "No refresh token present", http.StatusBadRequest)
		return
	}
	tokens, err := handler.authService.Refresh(r.Context(), refreshCookie.Value)
	if err != nil {
		http.Error(w, "Error while refresh", http.StatusInternalServerError)
		return
	}
	writeRefreshTokenInHttpCookie(w, tokens.RefreshToken)
	_, err = io.WriteString(w, tokens.AccessToken)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
}
