namespace backend.kafka.requests.ContactPerson;

public record ContactPersonDeleteRequest(string businessClientEmail, long personId);