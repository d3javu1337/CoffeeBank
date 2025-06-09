using backend.model;

namespace backend.kafka.requests;

public record PaymentAccountCreateRequest(long ClientId, string Email);