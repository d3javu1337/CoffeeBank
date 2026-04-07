package main

import (
	"AuthMS/config"
	"AuthMS/handlers"
	"AuthMS/infra"
	"AuthMS/repositry/mongodb"
	"AuthMS/repositry/postgres"
	"AuthMS/routes"
	"AuthMS/service/auth"
	"AuthMS/service/jwt"
	"AuthMS/service/outbox"
	"AuthMS/service/security"
	"AuthMS/service/session"
	"context"
	"fmt"
	"log"
	"log/slog"
	"net/http"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.mongodb.org/mongo-driver/v2/mongo"
)

func main() {

	// app context
	appCtx := context.Background()

	// cfg load
	cfg, err := config.LoadConfig(appCtx)
	if err != nil {
		slog.Error("Config load error: ", err)
	}

	// infra init
	mongoClient, err := infra.NewMongoClient(appCtx, cfg.GetAppConfig().MongoConfig)
	mongoCollection := mongoClient.Database(cfg.GetAppConfig().MongoConfig.Database).Collection(cfg.GetAppConfig().MongoConfig.Collection)
	mongoCollection.Name()

	connectionPool, err := infra.NewPostgresConnection(appCtx, cfg.GetAppConfig().PostgresConfig)

	kafkaClient := infra.NewKafkaClientImpl(cfg.GetAppConfig().KafkaConfig)

	// defer release resources

	if mongoClient != nil {
		defer func(mongo *mongo.Client) {
			shutdownContext, cancel := context.WithTimeout(context.Background(), time.Second*5)
			defer cancel()
			err := mongo.Disconnect(shutdownContext)
			if err != nil {
				slog.Error("Error while disconnecting from mongodb: ", "error", err)
			}
			slog.Info("Closed mongo connection")
		}(mongoClient)
	}

	if connectionPool != nil {
		defer func(pg *pgxpool.Pool) {
			connectionPool.Close()
			slog.Info("Closed Postgres connection")
		}(connectionPool)
	}

	if kafkaClient != nil {
		defer kafkaClient.Destroy()
	}

	// repo
	authRepo := postgres.NewClientAuthRepositoryImpl(connectionPool)
	//sessionsRepo := mongodb.NewSessionsRepositoryImpl(mongoCollection)
	sessionsRepo := mongodb.NewSessionsRepositoryPGImpl(connectionPool)
	outboxRepo := postgres.NewOutboxRepositoryImpl(connectionPool)

	// service
	securityService := new(security.SecurityServiceImpl)
	outboxService := outbox.NewOutboxServiceImpl(kafkaClient, outboxRepo)
	jwtService := jwt.NewJWTServiceImpl(cfg.GetAppConfig().JWTConfig)
	sessionService := session.NewSessionServiceImpl(sessionsRepo)
	authService := auth.NewClientAuthServiceImpl(authRepo, outboxRepo, sessionService, securityService, jwtService)

	// outbox workers start
	outboxService.StartWorkers(appCtx)

	// handlers
	authHandler := handlers.NewAuthHandlerImpl(authService)
	sessionsHandler := handlers.NewSessionsHandlerImpl(sessionService)

	// middlewares
	secureMiddleware := handlers.NewSecureMiddleware(jwtService, sessionService)

	appMux := routes.BuildAppMux(authHandler, sessionsHandler, secureMiddleware)

	log.Fatal(http.ListenAndServe(fmt.Sprintf(":%d", cfg.GetAppConfig().WebServerConfig.Port), appMux))

}
