package errors

import (
	"errors"
	"fmt"
)

var ClientAlreadyExistsError = errors.New("client already exists")

type WrongStringRepresentationOfRoleError struct {
	arg string
}

func NewWrongStringRepresentationOfRoleError(value string) *WrongStringRepresentationOfRoleError {
	return &WrongStringRepresentationOfRoleError{arg: value}
}

func (e *WrongStringRepresentationOfRoleError) Error() string {
	return fmt.Sprintf("Wrong string representation of role: %s", e.arg)
}
