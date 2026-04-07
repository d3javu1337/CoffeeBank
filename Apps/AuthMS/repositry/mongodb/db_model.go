package mongodb

import (
	"AuthMS/model/session"
	"time"
)

type SessionDto struct {
	Id                  string    `bson:"_id,omitempty"`
	OpenedAt            time.Time `bson:"openedAt,omitempty"`
	LastInteractionTime time.Time `bson:"lastInteractionTime,omitempty"`
	UserAgent           string    `bson:"userAgent"`
	AccessToken         string    `bson:"accessToken,omitempty"`
	RefreshToken        string    `bson:"refreshToken,omitempty"`
}

type SessionsDto struct {
	Id       string        `bson:"_id,omitempty"`
	Sessions []*SessionDto `bson:"sessions,omitempty"`
}

func SessionDtoFromModel(modelType *session.Session) *SessionDto {
	return &SessionDto{
		Id:                  modelType.Id(),
		OpenedAt:            modelType.OpenedAt(),
		LastInteractionTime: modelType.LastInteractionTime(),
		UserAgent:           modelType.UserAgent(),
		AccessToken:         modelType.AccessToken(),
		RefreshToken:        modelType.RefreshToken(),
	}
}

func SessionDtoToModel(dtoType *SessionDto) *session.Session {
	return session.SessionConstructor(
		dtoType.Id,
		dtoType.OpenedAt,
		dtoType.LastInteractionTime,
		dtoType.UserAgent,
		dtoType.AccessToken,
		dtoType.RefreshToken,
	)
}
