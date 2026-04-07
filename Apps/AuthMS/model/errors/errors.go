package errors

import "fmt"

type ClientAlreadyExistsError struct {
	arg string
}

func NewClientAlreadyExistsError(email string) *ClientAlreadyExistsError {
	return &ClientAlreadyExistsError{arg: email}
}

func (e *ClientAlreadyExistsError) Error() string {
	return fmt.Sprintf("Client with email %s already exists", e.arg)
}

type WrongStringRepresentationOfRoleError struct {
	arg string
}

func NewWrongStringRepresentationOfRoleError(value string) *WrongStringRepresentationOfRoleError {
	return &WrongStringRepresentationOfRoleError{arg: value}
}

func (e *WrongStringRepresentationOfRoleError) Error() string {
	return fmt.Sprintf("Wrong string representation of role: %s", e.arg)
}
