using backend.dto.payment;
using backend.model;
using backend.repository;

namespace backend.service;

public class PaymentService(PaymentRepository repository, PaymentAccountService paymentAccountService)
{
    public PaymentWithAmountDto? GetPayment(string businessClientEmail, Guid paymentId)
    {
        var payment =  repository.Find(paymentId).Result;
        return payment.ProviderPaymentAccount.Id != paymentAccountService.Get(businessClientEmail)!.Id ? null : 
            repository.FindPaymentById(paymentId).Result;
    }

    public List<PaymentWithAmountDto> GetAllPaymentsByPaymentAccountId(long paymentAccountId)
    {
        return repository.FindAllByPaymentAccount(paymentAccountId).Result;
    }

    public bool CheckPayment(Guid invoiceId)
    {
        return repository.CheckPayment(invoiceId).Result;
    }
}