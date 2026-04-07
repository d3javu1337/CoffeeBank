package postgres

import (
	"AuthMS/model/outbox"
	"context"
	"log/slog"
	"time"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
)

type OutboxRepository interface {
	InsertRegistrationRecord(ctx context.Context, transaction pgx.Tx, id string, serializedDto []byte, registrationType outbox.RegistrationOutboxType) error
	DeleteRegistrationRecord(ctx context.Context, id string) error
	FindRecordsWithRetryNotAfterNow(ctx context.Context) (pgx.Tx, []*RegistrationOutboxDto, error)
	UpdateRecord(ctx context.Context, transaction pgx.Tx, id string) error
}

type OutboxRepositoryImpl struct {
	pool *pgxpool.Pool
}

func NewOutboxRepositoryImpl(pool *pgxpool.Pool) *OutboxRepositoryImpl {
	return &OutboxRepositoryImpl{pool: pool}
}

func (repo *OutboxRepositoryImpl) InsertRegistrationRecord(
	ctx context.Context,
	transaction pgx.Tx,
	id string,
	serializedDto []byte,
	registrationType outbox.RegistrationOutboxType,
) error {

	_, err := transaction.Exec(
		ctx,
		"insert into registration_outbox(client_id, payload, type) values ($1, $2, $3)",
		id, serializedDto, registrationType)
	if err != nil {
		slog.Error("Error insert into outbox", "error", err)
		return err
	}
	return nil
}

func (repo *OutboxRepositoryImpl) DeleteRegistrationRecord(ctx context.Context, id string) error {
	transaction, err := repo.pool.Begin(ctx)
	defer func(transaction pgx.Tx, ctx context.Context) {
		_ = transaction.Rollback(ctx)
	}(transaction, ctx)
	if err != nil {
		slog.Error("Error transaction begin", "error", err)
		return err
	}
	var clientId string
	_ = transaction.QueryRow(ctx, "select ro.client_id from registration_outbox ro where ro.client_id=$1 for update", id).Scan(&clientId)

	_, err = transaction.Exec(ctx, "delete from registration_outbox ro where ro.client_id=$1", clientId)
	if err != nil {
		slog.Error("Delete error", "error", err)
		return err
	}
	_ = transaction.Commit(ctx)
	return nil
}

func (repo *OutboxRepositoryImpl) FindRecordsWithRetryNotAfterNow(ctx context.Context) (pgx.Tx, []*RegistrationOutboxDto, error) {
	transaction, err := repo.pool.Begin(ctx)
	if err != nil {
		slog.Error("Transaction begin error", "error", err)
		return nil, nil, err
	}
	v, err := transaction.Query(ctx, "select ro.client_id, ro.payload, ro.type, ro.creation_time, ro.next_retry_time from registration_outbox ro where ro.next_retry_time <= $1 for update limit 10", time.Now())
	if err != nil {
		return nil, nil, err
	}
	var res []*RegistrationOutboxDto
	for v.Next() {
		var ob RegistrationOutboxDto
		err := v.Scan(&ob.ClientId, &ob.Payload, &ob.Type, &ob.CreationTime, &ob.NextRetryTime)
		if err != nil {
			slog.Error("Error scanning", "error", err)
			continue
		}
		res = append(res, &ob)
	}
	return transaction, res, nil
}

func (repo *OutboxRepositoryImpl) UpdateRecord(ctx context.Context, transaction pgx.Tx, id string) error {
	_, err := transaction.Exec(ctx, "update registration_outbox set next_retry_time=$2 where client_id=$1", id, time.Now().Add(time.Second*15))
	if err != nil {
		slog.Error("Postgres outbox record update error", "error", err)
		return err
	}
	return nil
}
