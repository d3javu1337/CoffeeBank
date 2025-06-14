namespace backend.dto.ContactPerson;

public record ContactPersonCreateDto(string Surname, string Name, string Patronymic, string PhoneNumber, string Email);