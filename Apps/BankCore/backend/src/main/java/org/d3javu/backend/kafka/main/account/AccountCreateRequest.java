package org.d3javu.backend.kafka.main.account;

public record AccountCreateRequest(
        Long id,
        String email
) {}