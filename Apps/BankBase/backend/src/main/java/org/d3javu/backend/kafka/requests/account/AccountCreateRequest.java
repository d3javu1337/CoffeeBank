package org.d3javu.backend.kafka.requests.account;

public record AccountCreateRequest(
        Long id,
        String email
) {}
