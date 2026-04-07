package handlers

import (
	"AuthMS/service/jwt"
	"AuthMS/service/session"
	"fmt"
	"net/http"
	"strings"
)

const AccessTokenPathParamName = "accessToken"
const EmailPathParamName = "email"

type SecureMiddleware struct {
	jwtService     jwt.JWTService
	sessionService session.SessionService
}

func NewSecureMiddleware(jwtService jwt.JWTService, sessionService session.SessionService) *SecureMiddleware {
	return &SecureMiddleware{
		jwtService:     jwtService,
		sessionService: sessionService,
	}
}

func (middleware *SecureMiddleware) WithSecure(next http.Handler) http.Handler {
	return http.HandlerFunc(
		func(w http.ResponseWriter, r *http.Request) {
			authHeader := r.Header.Get("Authorization")
			accessToken := strings.TrimPrefix(authHeader, "Bearer ")
			email, err := middleware.jwtService.GetEmail(accessToken, jwt.ACCESS)
			exist, _ := middleware.sessionService.ExistSessionByAccessToken(r.Context(), email, accessToken)
			if err != nil {
				w.Header().Set("WWW-Authenticate", `Bearer realm="Access"`)
				http.Error(w, fmt.Sprintf("JWT error: %s", err.Error()), http.StatusUnauthorized)
				return
			}
			if !exist {
				w.Header().Set("WWW-Authenticate", `Bearer realm="Access"`)
				http.Error(w, "Not found session with this access token. Please try login again", http.StatusUnauthorized)
				return
			}
			_, _ = middleware.sessionService.UpdateSessionOnInteraction(r.Context(), email, accessToken)
			r.SetPathValue(AccessTokenPathParamName, accessToken)
			r.SetPathValue(EmailPathParamName, email)
			next.ServeHTTP(w, r)
		},
	)
}
