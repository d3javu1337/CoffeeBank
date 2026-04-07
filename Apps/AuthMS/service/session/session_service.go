package session

import (
	"AuthMS/model/session"
	"AuthMS/repositry/mongodb"
	"context"
)

type SessionService interface {
	RegisterSession(ctx context.Context, email string, userAgent string, accessToken string, refreshToken string) (bool, error)
	Logout(ctx context.Context, email string, accessToken string) (bool, error)
	CloseAllSessionsExclude(ctx context.Context, email string, accessToken string) (bool, error)
	CloseSession(ctx context.Context, email string, sessionId string) (bool, error)
	UpdateSessionOnTokenRefresh(ctx context.Context, email string, oldRefreshToken string, newAccessToken string, newRefreshToken string) (bool, error)
	UpdateSessionOnInteraction(ctx context.Context, email string, accessToken string) (bool, error)
	GetAllSessions(ctx context.Context, email string) ([]*session.Session, error)
	GetSession(ctx context.Context, email string, sessionId string) (*session.Session, error)
	ExistSessionByAccessToken(ctx context.Context, email string, accessToken string) (bool, error)
}

type SessionServiceImpl struct {
	repo mongodb.SessionsRepository
}

func NewSessionServiceImpl(repo mongodb.SessionsRepository) *SessionServiceImpl {
	return &SessionServiceImpl{
		repo: repo,
	}
}

func (service *SessionServiceImpl) RegisterSession(ctx context.Context, email string, userAgent string, accessToken string, refreshToken string) (bool, error) {
	sess, err := session.NewSession(userAgent, accessToken, refreshToken)
	if err != nil {
		return false, err
	}
	return service.repo.RegisterSession(ctx, email, mongodb.SessionDtoFromModel(sess))
}

func (service *SessionServiceImpl) Logout(ctx context.Context, email string, accessToken string) (bool, error) {
	return service.repo.CloseSessionByAccessToken(ctx, email, accessToken)
}

func (service *SessionServiceImpl) CloseAllSessionsExclude(ctx context.Context, email string, accessToken string) (bool, error) {
	return service.repo.CloseAllSessionsExclude(ctx, email, accessToken)
}

func (service *SessionServiceImpl) CloseSession(ctx context.Context, email string, sessionId string) (bool, error) {
	return service.repo.CloseSessionById(ctx, email, sessionId)
}

func (service *SessionServiceImpl) UpdateSessionOnTokenRefresh(ctx context.Context, email string, oldRefreshToken string, newAccessToken string, newRefreshToken string) (bool, error) {
	return service.repo.UpdateSessionOnTokenRefresh(
		ctx,
		email,
		oldRefreshToken,
		newAccessToken,
		newRefreshToken,
	)
}

func (service *SessionServiceImpl) UpdateSessionOnInteraction(ctx context.Context, email string, accessToken string) (bool, error) {
	return service.repo.UpdateSessionOnInteraction(ctx, email, accessToken)
}

func (service *SessionServiceImpl) GetAllSessions(ctx context.Context, email string) ([]*session.Session, error) {
	sessions, err := service.repo.FindAllSessions(ctx, email)
	if err != nil {
		return nil, err
	}
	var res []*session.Session
	for _, v := range sessions.Sessions {
		res = append(res, mongodb.SessionDtoToModel(v))
	}
	return res, nil
}

func (service *SessionServiceImpl) GetSession(ctx context.Context, email string, sessionId string) (*session.Session, error) {
	sessionDto, err := service.repo.FindSession(ctx, email, sessionId)
	if err != nil {
		return nil, err
	}
	return mongodb.SessionDtoToModel(sessionDto), nil
}

func (service *SessionServiceImpl) ExistSessionByAccessToken(ctx context.Context, email string, accessToken string) (bool, error) {
	return service.repo.ExistSessionByAccessToken(ctx, email, accessToken)
}
