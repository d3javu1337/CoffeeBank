package jwt

import (
	"AuthMS/config"
	"AuthMS/model/principal"
	"slices"
	"testing"
	"time"
)

var (
	cfg = config.JWTConfig{
		AccessSecret:  "AccessSecret",
		RefreshSecret: "RefreshSecret",
	}
	service       = NewJWTServiceImpl(&cfg)
	expectedEmail = "test"
	roles         = []principal.Role{principal.BASE_CLIENT}
	pr            = principal.NewPrincipal(expectedEmail, roles)
	location, _   = time.LoadLocation("GMT")
	baseDate      = time.Date(2026, 03, 10, 0, 0, 0, 0, location)
	accessExp     = baseDate.Add(time.Minute * 15)
	refreshExp    = baseDate.AddDate(0, 1, 0)
	accessToken   = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJybHMiOlsiQkFTRV9DTElFTlQiXSwic3ViIjoidGVzdCIsImV4cCI6MTc3MzEwMTcwMCwiaWF0IjoxNzczMTAwODAwfQ.4CKJGNzPsp10InAyJM52gKRV5TsWtCaQQfCW111N560"
	refreshToken  = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJybHMiOlsiQkFTRV9DTElFTlQiXSwic3ViIjoidGVzdCIsImV4cCI6MTc3NTc3OTIwMCwiaWF0IjoxNzczMTAwODAwfQ.AUNHIUrLoqENGDzkyXyw5I0L-6AZPolpFB918RHncA0"
)

func TestJWTServiceImpl_GenerateAccessToken(t *testing.T) {
	token, err := service.generateAccessToken(&pr, baseDate, accessExp)
	if err != nil {
		t.Errorf("Unexpected error result: %v", err)
	}
	if token != accessToken {
		t.Errorf("Wrong token: expected: %s. Current: %s", accessToken, token)
	}
}

func TestJWTServiceImpl_GenerateRefreshToken(t *testing.T) {
	token, err := service.generateRefreshToken(&pr, baseDate, refreshExp)
	if err != nil {
		t.Errorf("Unexpected error result: %v", err)
	}
	if token != refreshToken {
		t.Errorf("Wrong token: expected: %s. Current: %s", refreshToken, token)
	}
}

func TestJWTServiceImpl_GetEmail(t *testing.T) {
	token, _ := service.GenerateAccessToken(&pr)
	email, _ := service.GetEmail(token, ACCESS)
	if email != expectedEmail {
		t.Errorf("Wrong email: expected: %s. Current: %s", expectedEmail, email)
	}
}

func TestJWTServiceImpl_GetExpiration(t *testing.T) {
	now := time.Now().Truncate(time.Second)
	exp := now.AddDate(0, 1, 0)
	token, _ := service.generateRefreshToken(&pr, now, exp)
	curr, _ := service.GetExpiration(token, REFRESH)
	if *curr != exp {
		t.Errorf("Wrong expiration date. Expected: %v. Current: %v", exp, *curr)
	}
}

func TestJWTServiceImpl_GetIssuedAt(t *testing.T) {
	now := time.Now().Truncate(time.Second)
	exp := now.AddDate(0, 1, 0)
	token, _ := service.generateRefreshToken(&pr, now, exp)
	issued, _ := service.GetIssuedAt(token, REFRESH)
	if *issued != now {
		t.Errorf("Wrong issue date. Expected: %v. Current: %v", now, *issued)
	}
}

func TestJWTServiceImpl_GetRoles(t *testing.T) {
	token, _ := service.GenerateAccessToken(&pr)
	currRoles, _ := service.GetRoles(token, ACCESS)
	if !slices.Equal(currRoles, roles) {
		t.Errorf("Wrong roles Expected: %v. Current: %v", roles, currRoles)
	}
}
