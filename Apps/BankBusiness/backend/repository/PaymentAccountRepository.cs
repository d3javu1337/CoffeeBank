using backend.model;
using Microsoft.EntityFrameworkCore;

namespace backend.repository;

public class PaymentAccountRepository(DatabaseContext _context)
{

    public async Task<PaymentAccount?> Find(long clientId)
    {
        return await _context.PaymentAccounts.FromSql($"select * from payment_account p where p.business_client_id={clientId}").FirstOrDefaultAsync();
    }

    public async Task<bool> isTokenValid(long paymentAccountId, Guid token)
    {
        return _context.PaymentAccounts.FindAsync(paymentAccountId).Result!.InvoiceCreateToken == token;
    }

    public async Task<Guid?> FindInvoiceCreateTokenByAccountId(long accountId)
    {
        return await _context.PaymentAccounts.Where(e => e.Id == accountId).Select(x => x.InvoiceCreateToken).FirstOrDefaultAsync();
    }

}