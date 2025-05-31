package org.d3javu.backend.kafka.main.business.paymentaccount;

public record PaymentAccountCreateRequest(
        Long clientId,
        String email
) {}
