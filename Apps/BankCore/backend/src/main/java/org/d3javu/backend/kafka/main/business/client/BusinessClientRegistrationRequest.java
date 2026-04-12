package org.d3javu.backend.kafka.main.business.client;

public record BusinessClientRegistrationRequest(
        String id,
        String officialName,
        String brand,
        String email
) {}
