using backend.dto.payment;
using backend.model;
using Microsoft.EntityFrameworkCore;

namespace backend.repository;

public class PaymentRepository(DatabaseContext _context)
{
    public async Task<Payment> Find(Guid id)
    {
        return await _context.Payments.FindAsync(id);
    }

    public async Task<List<PaymentWithAmountDto>> FindAllByPaymentAccount(long paymentAccountId)
    {
        var t = _context.Payments
            .Where(p => p.ProviderPaymentAccount.Id == paymentAccountId)
            .Select(p => new 
            {
                paymentId = p.Id,
                invoiceId = p.Invoice.Id
            }).ToList();
        var t1 = _context.Invoices
            .Where(i => t.Any(tpl => tpl.invoiceId == i.Id))
            .Select(i => new {i.amount, i.Id})
            .ToList();
        return t.Join(
            t1,
            tpl1 => tpl1.invoiceId,
            tpl2 => tpl2.Id,
            (tpl1, tpl2) => new PaymentWithAmountDto(tpl1.paymentId, tpl2.amount)
        ).ToList();
        // return await _context.Payments.FromSqlRaw($"select p.id from payment p where p.payment_account_id = {paymentAccountId}").ToListAsync();
    }

    public async Task<PaymentWithAmountDto?> FindPaymentById(Guid paymentId)
    {
        var t = await _context.Payments
            .Where(p => p.Id == paymentId)
            .Select(p => new
            {
                paymentId = p.Id,
                invoiceId = p.Invoice.Id
            }).FirstOrDefaultAsync();
        var t1 = _context.Invoices
            .Where(i => i.Id == t!.invoiceId)
            .Select(i => new {i.amount, i.Id})
            .FirstOrDefaultAsync();
        return new PaymentWithAmountDto(t.paymentId, t1.Result!.amount);
    }

    public async Task<bool> CheckPayment(Guid invoiceId)
    {
        return (await _context.Payments.FromSql($"select * from payment p where p.invoice_id = {invoiceId}").CountAsync()) == 1;
    }
    
}