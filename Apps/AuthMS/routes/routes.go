package routes

import (
	"AuthMS/handlers"
	"net/http"
)

func BuildAppMux(
	authHandler handlers.AuthHandler,
	sessionsHandler handlers.SessionsHandler,
	middleware *handlers.SecureMiddleware,
) *http.ServeMux {
	appMux := http.NewServeMux()

	//auth
	authMux := http.NewServeMux()
	authMux.HandleFunc("POST /login", authHandler.Login)
	authMux.Handle("GET /logout", middleware.WithSecure(http.HandlerFunc(authHandler.Logout)))
	authMux.HandleFunc("POST /registration/base", authHandler.BaseRegistration)
	authMux.HandleFunc("POST /registration/business", authHandler.BusinessRegistration)
	authMux.HandleFunc("GET /refresh", authHandler.Refresh)

	//sessions
	sessionsMux := http.NewServeMux()
	sessionsMux.HandleFunc("POST /close/{id}", sessionsHandler.CloseSession)
	sessionsMux.HandleFunc("POST /close", sessionsHandler.CloseAllSessionsExclude)
	sessionsMux.HandleFunc("GET /{id}", sessionsHandler.GetSession)
	sessionsMux.HandleFunc("GET /", sessionsHandler.GetAllSessions)

	appMux.Handle("/auth/", http.StripPrefix("/auth", authMux))
	appMux.Handle("/sessions/", http.StripPrefix("/sessions", middleware.WithSecure(sessionsMux)))

	return appMux
}
