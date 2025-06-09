using backend.model;
using Microsoft.EntityFrameworkCore;

namespace backend.repository;


public class BusinessClientRepository(DatabaseContext context)
{
    private readonly DatabaseContext context = context;

    public async Task Create(BusinessClient businessClient)
    {
        await context.BusinessClients.AddAsync(businessClient);
    }

    public async Task<BusinessClient> Find(long id)
    {
        return await context.BusinessClients.FindAsync(id);
    }    
    
    public async Task<BusinessClient> FindByEmail(string email)
    {
        return await context.BusinessClients.FromSql($"select * from business_client b where b.email={email}").FirstAsync();
    }
}
