package infra

import (
	"AuthMS/config"
	"context"
	"fmt"
	"log/slog"

	//"github.com/jackc/pgx"
	"github.com/jackc/pgx/v5/pgxpool"
)

func NewPostgresConnection(ctx context.Context, config *config.PostgresConfig) (*pgxpool.Pool, error) {
	connString := fmt.Sprintf("postgresql://%v:%v@%v:%v/%v", config.Username, config.Password, config.Host, config.Port, config.Database)
	if conn, err := pgxpool.New(ctx, connString); err != nil {
		slog.Error("failed to create connection to db", err)
		return nil, err
	} else {
		slog.Info("created connection to db")
		if err := conn.Ping(ctx); err != nil {
			slog.Error("Ping to db failed")
		}
		return conn, nil
	}

}
