using backend.dto.BusinessClient;
using backend.kafka;
using backend.kafka.requests;
using backend.model;
using backend.repository;

namespace backend.service;

public class BusinessClientService
{
    private readonly BusinessClientRepository _repository;
    private readonly KafkaProducer _producer;
    private readonly SecurityService _securityService;

    public BusinessClientService(BusinessClientRepository repository, KafkaProducer producer,
        SecurityService securityService)
    {
        _repository = repository;
        _producer = producer;
        _securityService = securityService;
    }

    public BusinessClient GetById(long id)
    {
        return _repository.Find(id).Result;
    }

    public async Task Registration(BusinessClientCreateDto dto)
    {
        _producer.produce("business-client-registration-topic",
                new BusinessClientCreateRequest(
                    dto.OfficialName,
                    dto.Brand,
                    dto.Email,
                    _securityService.HashPassword(dto.Password))
            );
    }
}