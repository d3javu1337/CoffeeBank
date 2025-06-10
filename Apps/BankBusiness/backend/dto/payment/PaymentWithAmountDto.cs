namespace backend.dto.payment;

public record PaymentWithAmountDto(Guid paymentId, double amount);