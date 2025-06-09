using backend.dto.BusinessClient;
using backend.kafka;
using backend.kafka.requests;
using backend.model;
using backend.repository;

namespace backend.service;

public class BusinessClientService(
    BusinessClientRepository repository,
    KafkaProducer producer,
    SecurityService securityService)
{
    public BusinessClient GetByEmail(string email)
    {
        return repository.Find(email).Result;
    }

    public async Task Registration(BusinessClientCreateDto dto)
    {
        await producer.produce("business_client_registration_topic",
                new BusinessClientCreateRequest(
                    dto.OfficialName,
                    dto.Brand,
                    dto.Email,
                    securityService.HashPassword(dto.Password))
            );
    }

    public long GetIdByEmail(string email)
    {
        return repository.FindIdByEmail(email).Result;
    }
}