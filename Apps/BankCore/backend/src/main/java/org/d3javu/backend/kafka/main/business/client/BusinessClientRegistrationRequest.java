package org.d3javu.backend.kafka.main.business.client;

public record BusinessClientRegistrationRequest(
        String officialName,
        String brand,
        String email,
        String passwordHash
) {}
