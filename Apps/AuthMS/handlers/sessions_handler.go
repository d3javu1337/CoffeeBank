package handlers

import (
	"AuthMS/handlers/dto"
	"AuthMS/service/session"
	"encoding/json"
	"net/http"
)

type SessionsHandler interface {
	CloseAllSessionsExclude(w http.ResponseWriter, r *http.Request)
	CloseSession(w http.ResponseWriter, r *http.Request)
	GetAllSessions(w http.ResponseWriter, r *http.Request)
	GetSession(w http.ResponseWriter, r *http.Request)
}

type SessionsHandlerImpl struct {
	sessionService session.SessionService
}

func NewSessionsHandlerImpl(service session.SessionService) *SessionsHandlerImpl {
	return &SessionsHandlerImpl{
		sessionService: service,
	}
}

func (handler *SessionsHandlerImpl) CloseAllSessionsExclude(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()
	accessToken := r.PathValue(AccessTokenPathParamName)
	email := r.PathValue(EmailPathParamName)
	_, err := handler.sessionService.CloseAllSessionsExclude(r.Context(), email, accessToken)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	w.WriteHeader(http.StatusOK)
}

func (handler *SessionsHandlerImpl) CloseSession(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()
	email := r.PathValue(EmailPathParamName)
	sessionId := r.PathValue("id")
	_, err := handler.sessionService.CloseSession(r.Context(), email, sessionId)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	w.WriteHeader(http.StatusOK)
}

func (handler *SessionsHandlerImpl) GetAllSessions(w http.ResponseWriter, r *http.Request) {
	email := r.PathValue(EmailPathParamName)
	sessions, err := handler.sessionService.GetAllSessions(r.Context(), email)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	var dtos []*dto.SessionDto
	for _, v := range sessions {
		dtos = append(dtos, dto.SessionDtoFromModel(v))
	}
	res, err := json.Marshal(dtos)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	_, _ = w.Write(res)
	w.WriteHeader(http.StatusOK)
}

func (handler *SessionsHandlerImpl) GetSession(w http.ResponseWriter, r *http.Request) {
	email := r.PathValue(EmailPathParamName)
	sessionId := r.PathValue("id")
	session, err := handler.sessionService.GetSession(r.Context(), email, sessionId)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	dto := dto.SessionDtoFromModel(session)
	res, err := json.Marshal(dto)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	_, _ = w.Write(res)
	w.WriteHeader(http.StatusOK)
}
