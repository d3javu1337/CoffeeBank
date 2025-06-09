using backend.model;
using Microsoft.EntityFrameworkCore;

namespace backend.repository;

public class PaymentRepository(DatabaseContext _context)
{
    public async Task<Payment> Find(Guid id)
    {
        return await _context.Payments.FindAsync(id);
    }

    public async Task<List<Payment>> FindAllByPaymentAccount(long paymentAccountId)
    {
        return await _context.Payments.FromSql($"select * from payment p where p.payment_account_id = {paymentAccountId}").ToListAsync();
    }

    public async Task<bool> CheckPayment(Guid invoiceId)
    {
        return (await _context.Payments.FromSql($"select * from payment p where p.invoice_id = {invoiceId}").CountAsync()) == 1;
    }
}