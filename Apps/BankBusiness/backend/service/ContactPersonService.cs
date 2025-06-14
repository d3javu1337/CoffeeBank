using backend.dto.ContactPerson;
using backend.kafka;
using backend.kafka.requests.ContactPerson;
using backend.model;
using backend.repository;

namespace backend.service;

public class ContactPersonService(ContactPersonRepository contactPersonRepository, 
    BusinessClientService businessClientService, KafkaProducer kafkaProducer)
{
    public List<ContactPerson> GetContactPersons(string businessClientEmail)
    {
        return contactPersonRepository.FindAllByBusinessClientId(businessClientService.GetIdByEmail(businessClientEmail)).Result;
    }

    public ContactPerson? GetContactPersonById(long personId)
    {
        return contactPersonRepository.FindById(personId).Result;
    }

    public async Task CreateContactPerson(string businessClientEmail, ContactPersonCreateDto dto)
    {
        await kafkaProducer.produce("contact-person_create_topic",
            new ContactPersonCreateRequest(
                businessClientEmail,
                dto.Surname,
                dto.Name,
                dto.Patronymic,
                dto.PhoneNumber,
                dto.Email
            )
        );
    }
}