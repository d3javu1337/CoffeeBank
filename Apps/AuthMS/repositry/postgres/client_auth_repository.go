package postgres

import (
	"AuthMS/model/auth_data"
	"AuthMS/model/principal"
	"context"
	"log/slog"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
)

type ClientAuthRepository interface {
	ExistsClientByEmail(ctx context.Context, email string) (*bool, error)
	LoadAuthData(ctx context.Context, email string) (*auth_data.AuthData, error)
	LoadRoles(ctx context.Context, id string) ([]principal.Role, error)
	FindClientIdByEmail(ctx context.Context, email string) (*string, error)
	InsertClientAuthData(ctx context.Context, data auth_data.AuthData) (pgx.Tx, *string, error)
}

type ClientAuthRepositoryImpl struct {
	connectionPool *pgxpool.Pool
}

func NewClientAuthRepositoryImpl(connectionPool *pgxpool.Pool) *ClientAuthRepositoryImpl {
	return &ClientAuthRepositoryImpl{connectionPool: connectionPool}
}

func (repo *ClientAuthRepositoryImpl) ExistsClientByEmail(ctx context.Context, email string) (*bool, error) {
	resp := repo.connectionPool.QueryRow(ctx, "select count(*) > 0 from client_auth_data cd where cd.email=$1", email)
	var res bool
	err := resp.Scan(&res)
	if err != nil {
		return nil, err
	}
	return &res, nil
}

func (repo *ClientAuthRepositoryImpl) LoadAuthData(ctx context.Context, email string) (*auth_data.AuthData, error) {
	resp := repo.connectionPool.QueryRow(ctx, "select cd.email, cd.password_hash from client_auth_data cd where cd.email = $1", email)
	var res AuthData
	err := resp.Scan(&res.Email, &res.PasswordHash)
	if err != nil {
		return nil, err
	}
	return &auth_data.AuthData{
		Email:        res.Email,
		PhoneNumber:  res.PhoneNumber,
		PasswordHash: res.PasswordHash,
	}, nil
}

func (repo *ClientAuthRepositoryImpl) LoadRoles(ctx context.Context, id string) ([]principal.Role, error) {
	resp, err := repo.connectionPool.Query(ctx, "select cr.role from client_roles cr where cr.client_id = $1", id)
	if err != nil {
		return nil, err
	}
	defer resp.Close()

	var res []principal.Role
	for resp.Next() {
		var role principal.Role
		if err := resp.Scan(&role); err != nil {
			slog.Warn("Error while transforming string role: ", err)
		} else {
			res = append(res, role)
		}
	}

	if err := resp.Err(); err != nil {
		return nil, err
	}
	return res, nil
}

func (repo *ClientAuthRepositoryImpl) FindClientIdByEmail(ctx context.Context, email string) (*string, error) {
	resp := repo.connectionPool.QueryRow(ctx, "select cd.id from client_auth_data cd where cd.email = $1", email)
	var res string
	err := resp.Scan(&res)
	if err != nil {
		return nil, err
	}
	return &res, nil
}

func (repo *ClientAuthRepositoryImpl) InsertClientAuthData(ctx context.Context, data auth_data.AuthData) (pgx.Tx, *string, error) {
	tr, err := repo.connectionPool.Begin(ctx)
	if err != nil {
		return nil, nil, err
	}
	if _, err := tr.Exec(
		ctx,
		"insert into client_auth_data(email, phone_number, password_hash) values ($1, null, $2)",
		data.Email, data.PasswordHash); err != nil {
		tr.Rollback(ctx)
		return nil, nil, err
	}
	row := tr.QueryRow(ctx, "select cd.id from client_auth_data cd where cd.email=$1", data.Email)
	var id string
	err = row.Scan(&id)
	if err != nil {
		_ = tr.Rollback(ctx)
		return nil, nil, err
	}

	return tr, &id, nil
}
