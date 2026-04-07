package principal

import (
	"AuthMS/model/errors"
	"encoding/json"
	"fmt"
	"slices"
)

type Principal struct {
	email string
	roles []Role
}

func NewPrincipal(email string, roles []Role) Principal {
	return Principal{
		email: email,
		roles: roles,
	}
}

func (p *Principal) Email() string {
	return p.email
}

func (p *Principal) Roles() []Role {
	return p.roles
}

type Role string

func (r *Role) UnmarshalJSON(data []byte) error {
	var s string
	if err := json.Unmarshal(data, &s); err != nil {
		return err
	}
	switch role := Role(s); {
	case slices.Contains(Values, role):
		*r = role
		return nil
	default:
		return fmt.Errorf("invalid string representation of role: %s", s)
	}

}

const (
	BASE_CLIENT             Role = "BASE_CLIENT"
	BASE_SUPPORT            Role = "BASE_SUPPORT"
	BUSINESS_CLIENT         Role = "BUSINESS_CLIENT"
	BUSINESS_CONTACT_PERSON Role = "BUSINESS_CONTACT_PERSON"
	BUSINESS_SUPPORT        Role = "BUSINESS_SUPPORT"
)

var Values = []Role{
	BASE_CLIENT,
	BASE_SUPPORT,
	BUSINESS_CLIENT,
	BUSINESS_CONTACT_PERSON,
	BUSINESS_SUPPORT,
}

func RoleFromString(stringRole string) (*Role, error) {
	maybeRole := Role(stringRole)
	if ind := slices.IndexFunc(Values, func(r Role) bool {
		return r == maybeRole
	}); ind < 0 {
		return nil, errors.NewWrongStringRepresentationOfRoleError(stringRole)
	} else {
		return &Values[ind], nil
	}
}
