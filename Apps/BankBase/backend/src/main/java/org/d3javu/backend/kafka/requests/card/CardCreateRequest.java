package org.d3javu.backend.kafka.requests.card;

public record CardCreateRequest(
        Long clientId,
        String email,
        Long accountId
) {}
