package infra

import (
	"AuthMS/config"
	"context"
	"fmt"

	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.mongodb.org/mongo-driver/v2/mongo/options"
)

func NewMongoClient(ctx context.Context, cfg *config.MongoConfig) (*mongo.Client, error) {
	mongoUri := fmt.Sprintf(
		"mongodb://%s:%s@%s:%d/%s",
		cfg.Username,
		cfg.Password,
		cfg.Host,
		cfg.Port,
		cfg.AuthDB,
	)
	opts := options.Client().ApplyURI(mongoUri)
	return mongo.Connect(opts)
}
