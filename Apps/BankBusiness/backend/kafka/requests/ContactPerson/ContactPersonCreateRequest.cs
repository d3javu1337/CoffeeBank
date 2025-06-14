namespace backend.kafka.requests.ContactPerson;

public record ContactPersonCreateRequest(
    string businessClientEmail,
    string surname,
    string name,
    string patronymic,
    string phoneNumber,
    string email
);