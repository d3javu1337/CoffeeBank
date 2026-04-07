package base_client

import "time"

type BaseRegistrationRequest struct {
	Surname     string
	Name        string
	Patronymic  string
	DateOfBirth *time.Time
	PhoneNumber string
	Email       string
	Password    string
}
