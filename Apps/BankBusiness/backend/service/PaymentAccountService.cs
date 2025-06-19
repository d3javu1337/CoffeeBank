using backend.dto.PaymentAccount;
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
    public PaymentAccountReadDto? Get(string clientEmail)
    {
        var account = paymentAccountRepository.Find(businessClientService.GetIdByEmail(clientEmail)).Result;
        if (account == null) return null;
        return new PaymentAccountReadDto(account.Id, account.name, account.deposit);
    }

    public Guid? GetInvoiceCreateTokenByEmail(string email)
    {
        return paymentAccountRepository.FindInvoiceCreateTokenByAccountId(businessClientService.GetIdByEmail(email)).Result;
    }
    public async Task Create(string clientEmail)
    {
        await kafkaProducer.produce("payment-account_create_topic",
            new PaymentAccountCreateRequest(
                businessClientService.GetIdByEmail(clientEmail),
                clientEmail
            ));
    }

    public bool IsTokenValid(string clientEmail, Guid token)
    {
        var id = Get(clientEmail)?.Id;
        if(id == null) return false;
        return paymentAccountRepository.isTokenValid(id.Value, token).Result;
    }
}