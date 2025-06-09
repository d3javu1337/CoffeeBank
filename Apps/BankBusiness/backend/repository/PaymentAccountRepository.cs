using backend.model;
using Microsoft.EntityFrameworkCore;

namespace backend.repository;

public class PaymentAccountRepository(DatabaseContext _context)
{

    public async Task<PaymentAccount> Find(long clientId)
    {
        return await _context.PaymentAccounts.FromSql($"select from payment_account p where p.client_id={clientId}").FirstAsync();
    }

}