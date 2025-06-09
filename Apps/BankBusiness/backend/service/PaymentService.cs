using backend.model;
using backend.repository;

namespace backend.service;

public class PaymentService(PaymentRepository repository)
{
    public Payment GetPayment(Guid paymentId)
    {
        return repository.Find(paymentId).Result;
    }

    public List<Payment> GetAllByPaymentAccount(long paymentAccountId)
    {
        return repository.FindAllByPaymentAccount(paymentAccountId).Result;
    }

    public bool CheckPayment(Guid invoiceId)
    {
        return repository.CheckPayment(invoiceId).Result;
    }
}