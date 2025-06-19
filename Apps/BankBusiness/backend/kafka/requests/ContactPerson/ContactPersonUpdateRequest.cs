namespace backend.kafka.requests.ContactPerson;

public record ContactPersonUpdateRequest(string businessClientEmail, long contactPersonId, string surname, 
    string name, string patronymic, string phoneNumber, string email);