package org.d3javu.backend.kafka.main.base.personalaccount;

public record PersonalAccountCreateRequest(
        Long id,
        String email
) {}