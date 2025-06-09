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
    public PaymentAccount Find(string email)
    {
        return paymentAccountRepository.Find(businessClientService.GetIdByEmail(email)).Result;
    }

    public async Task Create(string email)
    {
        await kafkaProducer.produce("payment_account_create_topic",
            new PaymentAccountCreateRequest(
                businessClientService.GetIdByEmail(email),
                email
            ));
    }
}