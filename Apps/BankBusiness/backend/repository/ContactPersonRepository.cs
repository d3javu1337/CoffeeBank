using backend.model;
using Microsoft.EntityFrameworkCore;

namespace backend.repository;

public class ContactPersonRepository(DatabaseContext _context)
{
    public async Task<List<ContactPerson>> FindAllByBusinessClientId(long businessClientId)
    {
        return await _context.ContactPersons.ToListAsync();
    }

    public async Task<ContactPerson?> FindById(long id)
    {
        return await _context.ContactPersons.FindAsync(id);
    }

    public async Task<string?> GetBusinessClientEmailByContactPersonId(long contactPersonId)
    {
        return await _context.ContactPersons
            .Where(x => x.Id == contactPersonId)
            .Select(x => x.LinledClient.Email)
            .FirstOrDefaultAsync();
    }
}