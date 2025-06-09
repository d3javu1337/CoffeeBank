namespace backend.kafka.requests;

public record BusinessClientCreateRequest(string OfficialName, string Brand, string Email, string PasswordHash);