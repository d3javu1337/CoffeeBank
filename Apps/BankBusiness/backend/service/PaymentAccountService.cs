using backend.kafka;
using backend.kafka.requests;
using backend.model;
using backend.repository;

namespace backend.service;

public class PaymentAccountService(
    BusinessClientService businessClientService,
    PaymentAccountRepository paymentAccountRepository,
    KafkaProducer kafkaProducer)
{
    public PaymentAccount? Find(string clientEmail)
    {
        return paymentAccountRepository.Find(businessClientService.GetIdByEmail(clientEmail)).Result;
    }

    public async Task Create(string clientEmail)
    {
        await kafkaProducer.produce("payment_account_create_topic",
            new PaymentAccountCreateRequest(
                businessClientService.GetIdByEmail(clientEmail),
                clientEmail
            ));
    }
}