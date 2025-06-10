using backend.model;
using Microsoft.EntityFrameworkCore;

namespace backend.repository;


public class BusinessClientRepository(DatabaseContext _context)
{
    public async Task<BusinessClient> Find(string email)
    {
        return await _context.BusinessClients.FromSql($"select * from business_client where email={email}").FirstAsync();
    }

    public async Task<BusinessClient?> FindByEmail(string email)
    {
        return await _context.BusinessClients.FromSql($"select * from business_client b where b.email={email}").FirstAsync();
    }

    public async Task<long> FindIdByEmail(string email)
    {
        return (await _context.BusinessClients.FromSql($"select * from business_client b where b.email={email}").FirstAsync()).Id;
    }
}
