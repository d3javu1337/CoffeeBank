package security

import (
	"log/slog"

	"golang.org/x/crypto/bcrypt"
)

type SecurityService interface {
	HashPassword(password string) (*string, error)
	VerifyPassword(password string, hash string) error
}

type SecurityServiceImpl struct{}

func (_ *SecurityServiceImpl) HashPassword(password string) (*string, error) {
	if hash, err := bcrypt.GenerateFromPassword([]byte(password), 13); err != nil {
		slog.Error("Password hashing error: ", err)
		return nil, err
	} else {
		hashed := string(hash)
		return &hashed, nil
	}
}

func (_ *SecurityServiceImpl) VerifyPassword(password string, hash string) error {
	return bcrypt.CompareHashAndPassword([]byte(hash), []byte(password))
}
