package dto

import (
	"AuthMS/model/session"
	"time"
)

type SessionDto struct {
	Id                  string    `json:"id,omitempty"`
	OpenedAt            time.Time `json:"openedAt,omitempty"`
	LastInteractionTime time.Time `json:"lastInteractionTime,omitempty"`
	UserAgent           string    `json:"userAgent"`
}

func SessionDtoFromModel(session *session.Session) *SessionDto {
	return &SessionDto{
		Id:                  session.Id(),
		OpenedAt:            session.OpenedAt(),
		LastInteractionTime: session.LastInteractionTime(),
		UserAgent:           session.UserAgent(),
	}
}
