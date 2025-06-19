namespace backend.dto.ContactPerson;

public record ContactPersonUpdateDto(long ContactPersonId, string Surname, string Name, string Patronymic, string PhoneNumber, string Email);