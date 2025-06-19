using backend.dto.ContactPerson;
using backend.kafka;
using backend.kafka.requests.ContactPerson;
using backend.model;
using backend.repository;

namespace backend.service;

public class ContactPersonService(
    ContactPersonRepository contactPersonRepository,
    BusinessClientService businessClientService,
    KafkaProducer kafkaProducer)
{
    public List<ContactPersonReadDto> GetContactPersons(string businessClientEmail)
    {
        return contactPersonRepository
            .FindAllByBusinessClientId(businessClientService.GetIdByEmail(businessClientEmail)).Result
            .Select(x => new ContactPersonReadDto(x.Surname, x.Name, x.Patronymic, x.PhoneNumber, x.Email))
            .ToList();
    }

    public ContactPersonReadDto? GetContactPersonById(long personId)
    {
        var person = contactPersonRepository.FindById(personId).Result;
        return person == null
            ? null
            : new ContactPersonReadDto(
                person.Surname,
                person.Name,
                person.Patronymic,
                person.PhoneNumber,
                person.Email
            );
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

    public async Task UpdateContactPerson(string businessClientEmail, ContactPersonUpdateDto dto)
    {
        await kafkaProducer.produce("contact-person_update_topic", 
            new ContactPersonUpdateRequest(
                businessClientEmail,
                dto.ContactPersonId,
                dto.Surname,
                dto.Name,
                dto.Patronymic,
                dto.PhoneNumber,
                dto.Email
                )
            );
    }

    public async Task DeleteContactPerson(string businessClientEmail, long personId)
    {
        await kafkaProducer.produce("contact-person_delete_topic", new ContactPersonDeleteRequest(businessClientEmail, personId));
    }

    public bool IsContactPersonLinkedToBusinessClient(string businessClientEmail, long personId)
    {
        return contactPersonRepository.GetBusinessClientEmailByContactPersonId(personId).Result == businessClientEmail;
    }
}