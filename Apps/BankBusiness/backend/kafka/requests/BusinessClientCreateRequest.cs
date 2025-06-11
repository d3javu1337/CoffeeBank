namespace backend.kafka.requests;

public record BusinessClientCreateRequest(string officialName, string brand, string email, string passwordHash);