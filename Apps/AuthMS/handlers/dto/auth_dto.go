package dto

import "time"

type BaseRegistrationDto struct {
	Surname     string     `json:"surname" validate:"required"`
	Name        string     `json:"name"  validate:"required"`
	Patronymic  string     `json:"patronymic"`
	DateOfBirth *time.Time `json:"dateOfBirth"  validate:"required"`
	PhoneNumber string     `json:"phoneNumber"  validate:"required"`
	Email       string     `json:"email"  validate:"required"`
	Password    string     `json:"password"  validate:"required"`
}

type BusinessRegistrationDto struct {
	OfficialName string `json:"officialName" validate:"required"`
	Brand        string `json:"brand" validate:"required"`
	Email        string `json:"email" validate:"required"`
	Password     string `json:"password" validate:"required"`
}

type LoginDto struct {
	Email    string `json:"email" validate:"required"`
	Password string `json:"password" validate:"required"`
}
