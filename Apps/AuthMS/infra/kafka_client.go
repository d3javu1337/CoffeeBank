package infra

import (
	"AuthMS/config"
	"context"
	"log"
	"log/slog"
	"strings"
	"time"

	"github.com/segmentio/kafka-go"
)

type KafkaClient struct {
	topics *config.KafkaTopicsConfig
	reader *kafka.Reader
	writer *kafka.Writer
	cfg    *config.KafkaConfig
}

func NewKafkaClientImpl(cfg *config.KafkaConfig) *KafkaClient {
	reader := kafka.NewReader(kafka.ReaderConfig{
		Brokers:     strings.Split(cfg.BootstrapServers, ","),
		GroupID:     cfg.ConsumerGroup,
		GroupTopics: []string{cfg.Topics.BaseRegistrationResponseTopic, cfg.Topics.BusinessRegistrationResponseTopic},
	})
	writer := kafka.NewWriter(kafka.WriterConfig{
		Brokers:      strings.Split(cfg.BootstrapServers, ","),
		MaxAttempts:  3,
		WriteTimeout: time.Second * 3,
		RequiredAcks: 1,
		Async:        true,
	})
	return &KafkaClient{
		topics: cfg.Topics,
		reader: reader,
		writer: writer,
		cfg:    cfg,
	}
}

func (client *KafkaClient) Destroy() {
	err := client.writer.Close()
	if err != nil {
		log.Fatal("Error on close kafka writer")
	}
	err = client.reader.Close()
	if err != nil {
		log.Fatal("Error on close kafka reader")
	}
	slog.Info("Closed kafka connection")
}

func (client *KafkaClient) SendBaseRegistrationRequest(ctx context.Context, id string, dto []byte) error {
	if err := client.writer.WriteMessages(ctx, kafka.Message{
		Topic: client.topics.BaseRegistrationRequestTopic,
		Key:   []byte(id),
		Value: dto,
		Time:  time.Now(),
	}); err != nil {
		slog.Error("Kafka send error", "topic", client.topics.BaseRegistrationRequestTopic, "error", err)
		return err
	}
	return nil
}

func (client *KafkaClient) SendBusinessRegistrationRequest(ctx context.Context, id string, dto []byte) error {
	if err := client.writer.WriteMessages(ctx, kafka.Message{
		Topic: client.topics.BusinessRegistrationRequestTopic,
		Key:   []byte(id),
		Value: dto,
		Time:  time.Now(),
	}); err != nil {
		slog.Error("Kafka send error", "topic", client.topics.BusinessRegistrationRequestTopic, "error", err)
		return err
	}
	return nil
}

func (client *KafkaClient) HandleRegistrationResponse(ctx context.Context) (*string, error) {
	msg, err := client.reader.ReadMessage(ctx)
	if err != nil {
		slog.Error("Kafka read error", "topic", client.topics.BusinessRegistrationResponseTopic, "error", err)
		return nil, err
	}
	res := string(msg.Value)
	return &res, nil

}
