using backend.dto.BusinessClient;
using backend.http.responses.BusinessClient;
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
    public BusinessClientReadDto GetDtoByEmail(string email)
    {
        var client = repository.Find(email).Result;
        return new BusinessClientReadDto(
            client.OfficialName,
            client.Brand
        );
    }

    public async Task Registration(BusinessClientCreateDto dto)
    {
        await producer.produce("business-client_registration_topic",
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