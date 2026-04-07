package mongodb

import (
	"context"
	"fmt"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
)

type SessionsRepositoryPGImpl struct {
	pool *pgxpool.Pool
}

func NewSessionsRepositoryPGImpl(pool *pgxpool.Pool) *SessionsRepositoryPGImpl {
	return &SessionsRepositoryPGImpl{pool: pool}
}

func (s *SessionsRepositoryPGImpl) RegisterSession(ctx context.Context, email string, session *SessionDto) (bool, error) {
	tr, err := s.pool.Begin(ctx)
	if err != nil {
		return false, err
	}
	defer func(tr pgx.Tx, ctx context.Context) {
		_ = tr.Rollback(ctx)
	}(tr, ctx)
	_, err = tr.Exec(ctx, "insert into session(id, user_agent, access_token, refresh_token) values($1, $2, $3, $4)", session.Id, session.UserAgent, session.AccessToken, session.RefreshToken)
	_, err = tr.Exec(ctx, "insert into sessions(id, session_id) values ($1, $2)", email, session.Id)
	tr.Commit(ctx)
	return true, nil
}

func (s *SessionsRepositoryPGImpl) CloseSessionByAccessToken(ctx context.Context, email string, accessToken string) (bool, error) {
	tr, err := s.pool.Begin(ctx)
	if err != nil {
		return false, err
	}
	defer func(tr pgx.Tx, ctx context.Context) {
		err := tr.Rollback(ctx)
		if err != nil {

		}
	}(tr, ctx)
	var id string
	tr.QueryRow(ctx, "select s.id from session s where s.access_token=$1", accessToken).Scan(&id)
	_, err = tr.Exec(ctx, "delete from sessions where id=$1 and session_id=$2", email, id)
	_, err = tr.Exec(ctx, "delete from session where access_token=$1", accessToken)
	tr.Commit(ctx)
	return true, err
}

func (s *SessionsRepositoryPGImpl) CloseSessionById(ctx context.Context, email string, sessionId string) (bool, error) {
	tr, err := s.pool.Begin(ctx)
	if err != nil {
		return false, err
	}
	defer func(tr pgx.Tx, ctx context.Context) {
		err := tr.Rollback(ctx)
		if err != nil {

		}
	}(tr, ctx)
	tr.Exec(ctx, "delete from sessions where id=$1 and session_id=$2", email, sessionId)
	tr.Exec(ctx, "delete from session where id=$1", sessionId)
	tr.Commit(ctx)
	return true, err
}

func (s *SessionsRepositoryPGImpl) CloseAllSessionsExclude(ctx context.Context, email string, accessToken string) (bool, error) {
	return false, fmt.Errorf("в падлу имплементировать CloseAllSessionsExclude")
}

func (s *SessionsRepositoryPGImpl) UpdateSessionOnTokenRefresh(ctx context.Context, email string, oldRefreshToken string, newAccessToken string, newRefreshToken string) (bool, error) {
	tr, err := s.pool.Begin(ctx)
	if err != nil {
		return false, err
	}
	defer func(tr pgx.Tx, ctx context.Context) {
		err := tr.Rollback(ctx)
		if err != nil {

		}
	}(tr, ctx)
	tr.Exec(ctx, "update session set access_token=$1, refresh_token=$2, last_interaction_time=now() where refresh_token=$3", newAccessToken, newRefreshToken, oldRefreshToken)
	tr.Commit(ctx)
	return true, err
}

func (s *SessionsRepositoryPGImpl) UpdateSessionOnInteraction(ctx context.Context, email string, accessToken string) (bool, error) {
	tr, err := s.pool.Begin(ctx)
	if err != nil {
		return false, err
	}
	defer func(tr pgx.Tx, ctx context.Context) {
		err := tr.Rollback(ctx)
		if err != nil {

		}
	}(tr, ctx)
	tr.Exec(ctx, "update session set last_interaction_time=now() where access_token=$1", accessToken)
	tr.Commit(ctx)
	return true, err
}

func (s *SessionsRepositoryPGImpl) FindAllSessions(ctx context.Context, email string) (*SessionsDto, error) {
	rows, err := s.pool.Query(ctx, "select s.id, s.opened_at, s.last_interaction_time, s.user_agent, s.access_token, s.refresh_token from session s where s.id in (select ss.session_id from sessions ss where ss.id = $1)", email)
	if err != nil {
		return nil, err
	}
	var sessions []*SessionDto
	for rows.Next() {
		var dto SessionDto
		rows.Scan(&dto.Id, &dto.OpenedAt, &dto.LastInteractionTime, &dto.UserAgent, &dto.AccessToken, &dto.RefreshToken)
		sessions = append(sessions, &dto)
	}
	return &SessionsDto{
		Id:       email,
		Sessions: sessions,
	}, nil
}

func (s *SessionsRepositoryPGImpl) FindSession(ctx context.Context, email string, sessionId string) (*SessionDto, error) {
	var dto SessionDto
	s.pool.QueryRow(ctx, "select s.id, s.opened_at, s.last_interaction_time, s.user_agent, s.access_token, s.refresh_token from session s where s.id=$1", sessionId).Scan(&dto.Id, &dto.OpenedAt, &dto.LastInteractionTime, &dto.UserAgent, &dto.AccessToken, &dto.RefreshToken)
	return &dto, nil
}

func (s *SessionsRepositoryPGImpl) ExistSessionByAccessToken(ctx context.Context, email string, accessToken string) (bool, error) {
	var res bool
	s.pool.QueryRow(ctx, "select count(*)>0 from session s where s.access_token=$1", accessToken).Scan(&res)
	return res, nil
}
