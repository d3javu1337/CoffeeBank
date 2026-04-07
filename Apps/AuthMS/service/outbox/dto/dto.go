package dto

import "time"

type BaseRegistrationDto struct {
	Surname     string     `json:"surname"`
	Name        string     `json:"name"`
	Patronymic  string     `json:"patronymic"`
	DateOfBirth *time.Time `json:"dateOfBirth"`
	PhoneNumber string     `json:"phoneNumber"`
	Email       string     `json:"email"`
}

type BusinessRegistrationDto struct {
	OfficialName string `json:"officialName"`
	Brand        string `json:"brand"`
	Email        string `json:"email"`
}
