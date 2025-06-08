using backend.model;
using backend.repository;

namespace backend.service;

public class BusinessClientService
{
    
    private readonly BusinessClientRepository _repository;

    public BusinessClientService(BusinessClientRepository repository)
    {
        this._repository = repository;
    }

    public BusinessClient GetById(long id)
    {
        return _repository.Find(id).Result;
    }

    // public async Task<BusinessClient> Registration(string officialName, string brand, string email, string password)
    // {
        // kafka
    // }
    
}