package mq

import "github.com/segmentio/kafka-go"

type KafkaService interface {
	SendRegistrationBase()
	SendRegistrationBusiness()
	WaitResponseBase()
	WaitResponseBusiness()
}

type KafkaServiceImpl struct {
	producer *kafka.Writer
	consumer *kafka.Reader
}

func NewKafkaServiceImpl(producer *kafka.Writer, consumer *kafka.Reader) *KafkaServiceImpl {
	return &KafkaServiceImpl{
		producer: producer,
		consumer: consumer,
	}
}
