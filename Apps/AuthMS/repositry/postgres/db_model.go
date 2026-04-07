package postgres

import (
	"AuthMS/model/outbox"
	"AuthMS/model/principal"
	"time"
)

type AuthData struct {
	Email        string
	PhoneNumber  *string
	PasswordHash string
}

type ClientDto struct {
	Id           int64
	Email        string
	PhoneNumber  string
	PasswordHash string
	IsEnabled    bool
}

type RegistrationOutboxDto struct {
	ClientId      string
	Payload       []byte
	Type          outbox.RegistrationOutboxType
	CreationTime  time.Time
	NextRetryTime time.Time
}

type Principal struct {
	email string
	id    int64
	roles []principal.Role
}
