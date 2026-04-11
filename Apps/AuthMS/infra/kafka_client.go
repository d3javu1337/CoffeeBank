package infra

import (
	"AuthMS/config"
	"AuthMS/model/outbox"
	"context"
	"fmt"
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

func (client *KafkaClient) SendRequest(ctx context.Context, id string, dto []byte, requestType outbox.RegistrationOutboxType) error {
	topic, err := client.getTopicNameByRequestType(requestType)
	if err != nil {
		return err
	}
	err = client.writer.WriteMessages(ctx, kafka.Message{
		Topic: *topic,
		Key:   []byte(id),
		Value: dto,
		Time:  time.Now(),
	})
	if err != nil {
		return err
	}
	return nil
}

func (client *KafkaClient) getTopicNameByRequestType(requestType outbox.RegistrationOutboxType) (*string, error) {
	switch requestType {
	case outbox.BASE:
		return &client.topics.BaseRegistrationRequestTopic, nil
	case outbox.BUSINESS:
		return &client.topics.BusinessRegistrationRequestTopic, nil
	default:
		return nil, fmt.Errorf("not found match for outbox type and topics")
	}
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
