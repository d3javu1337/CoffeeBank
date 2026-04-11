package outbox

import (
	"AuthMS/model/outbox"
	"AuthMS/repositry/postgres"
	"AuthMS/service/mq"
	"context"
	"log/slog"
	"time"

	"github.com/jackc/pgx/v5"
)

type OutboxService interface {
	StartWorkers(ctx context.Context)
}

type OutboxServiceImpl struct {
	repo         postgres.OutboxRepository
	kafkaService mq.KafkaService
}

func NewOutboxServiceImpl(kafkaService mq.KafkaService, repo postgres.OutboxRepository) *OutboxServiceImpl {
	return &OutboxServiceImpl{
		kafkaService: kafkaService,
		repo:         repo,
	}
}

func (service *OutboxServiceImpl) startScheduledProducer(ctx context.Context) {
	ticker := time.NewTicker(time.Second)
	defer ticker.Stop()
	for {
		select {
		case <-ctx.Done():
			slog.Error("Outbox producer ctx done")
			return
		case <-ticker.C:
			{
				transaction, vals, err := service.repo.FindRecordsWithRetryNotAfterNow(ctx)
				defer func(transaction pgx.Tx, ctx context.Context) {
					_ = transaction.Rollback(ctx)
				}(transaction, ctx)
				if err != nil {
					slog.Error("FindRecordsWithRetryNotAfterNow error", "error", err)
					continue
				}
				for _, record := range vals {
					var err error
					if record.Type == outbox.BASE {
						err = service.kafkaService.SendRegistrationBase(ctx, record.ClientId, record.Payload)
					} else {
						err = service.kafkaService.SendRegistrationBusiness(ctx, record.ClientId, record.Payload)
					}
					if err == nil {
						_ = service.repo.UpdateRecord(ctx, transaction, record.ClientId)
					}
				}
				_ = transaction.Commit(ctx)
			}
		}
	}
}

func (service *OutboxServiceImpl) startScheduledConsumer(ctx context.Context) {
	ticker := time.NewTicker(time.Millisecond * 500)
	defer ticker.Stop()
	for {
		select {
		case <-ctx.Done():
			slog.Error("Outbox consumer ctx done")
			return
		case <-ticker.C:
			{
				for i := 0; i < 10; i++ {
					id, err := service.kafkaService.HandleResponse(ctx)
					if err != nil {
						slog.Error("Kafka error", "error", err)
						continue
					}
					_ = service.repo.DeleteRegistrationRecord(ctx, *id)
				}
			}
		}
	}

}

func (service *OutboxServiceImpl) StartWorkers(ctx context.Context) {
	go service.startScheduledConsumer(ctx)
	go service.startScheduledProducer(ctx)
}
