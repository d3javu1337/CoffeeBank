package session

import "time"
import "github.com/google/uuid"

type Session struct {
	id                  string
	openedAt            time.Time
	lastInteractionTime time.Time
	userAgent           string
	accessToken         string
	refreshToken        string
}

func (s *Session) Id() string {
	return s.id
}

func (s *Session) OpenedAt() time.Time {
	return s.openedAt
}

func (s *Session) LastInteractionTime() time.Time {
	return s.lastInteractionTime
}

func (s *Session) UserAgent() string {
	return s.userAgent
}

func (s *Session) AccessToken() string {
	return s.accessToken
}

func (s *Session) RefreshToken() string {
	return s.refreshToken
}

type Sessions struct {
	id       string
	sessions []*Session
}

func (s *Sessions) Id() string {
	return s.id
}

func (s *Sessions) Sessions() []*Session {
	return s.sessions
}

func NewSession(userAgent string, accessToken string, refreshToken string) (*Session, error) {
	sessionId, err := uuid.NewRandom()
	if err != nil {
		return nil, err
	}
	return &Session{
		id:                  sessionId.String(),
		openedAt:            time.Now(),
		lastInteractionTime: time.Now(),
		userAgent:           userAgent,
		accessToken:         accessToken,
		refreshToken:        refreshToken,
	}, nil
}

func SessionConstructor(
	id string,
	openedAt time.Time,
	lastInteractionTime time.Time,
	userAgent string,
	accessToken string,
	refreshToken string,
) *Session {
	return &Session{
		id:                  id,
		openedAt:            openedAt,
		lastInteractionTime: lastInteractionTime,
		userAgent:           userAgent,
		accessToken:         accessToken,
		refreshToken:        refreshToken,
	}
}

func SessionsConstructor(
	id string,
	sessions []*Session,
) *Sessions {
	return &Sessions{
		id:       id,
		sessions: sessions,
	}
}
