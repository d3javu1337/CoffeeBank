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
        // return await context.BusinessClients.FromSql("select ")
        return await context.BusinessClients.FindAsync(id);
    }
}
