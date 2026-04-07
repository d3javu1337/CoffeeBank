package outbox

import "time"

type RegistrationOutboxType string

const (
	BASE     RegistrationOutboxType = "BASE"
	BUSINESS RegistrationOutboxType = "BUSINESS"
)

type RegistrationOutbox struct {
	clientId         string
	payload          []byte
	registrationType RegistrationOutboxType
	creationTime     time.Time
	nextRetryTime    time.Time
}

func NewRegistrationOutbox(
	clientId string,
	payload []byte,
	registrationType RegistrationOutboxType,
	creationTime time.Time,
	nextRetryTime time.Time,
) *RegistrationOutbox {
	return &RegistrationOutbox{
		clientId:         clientId,
		payload:          payload,
		registrationType: registrationType,
		creationTime:     creationTime,
		nextRetryTime:    nextRetryTime,
	}
}
