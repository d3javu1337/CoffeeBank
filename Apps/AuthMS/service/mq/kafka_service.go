package mq

import (
	"AuthMS/infra"
	"AuthMS/model/outbox"
	"context"
)

type KafkaService interface {
	SendRegistrationBase(ctx context.Context, id string, dto []byte) error
	SendRegistrationBusiness(ctx context.Context, id string, dto []byte) error
	HandleResponse(ctx context.Context) (*string, error)
}

type KafkaServiceImpl struct {
	client *infra.KafkaClient
}

func NewKafkaServiceImpl(client *infra.KafkaClient) *KafkaServiceImpl {
	return &KafkaServiceImpl{
		client: client,
	}
}

func (service *KafkaServiceImpl) SendRegistrationBase(ctx context.Context, id string, dto []byte) error {
	return service.client.SendRequest(ctx, id, dto, outbox.BASE)
}

func (service *KafkaServiceImpl) SendRegistrationBusiness(ctx context.Context, id string, dto []byte) error {
	return service.client.SendRequest(ctx, id, dto, outbox.BUSINESS)
}

func (service *KafkaServiceImpl) HandleResponse(ctx context.Context) (*string, error) {
	return service.client.HandleRegistrationResponse(ctx)
}
