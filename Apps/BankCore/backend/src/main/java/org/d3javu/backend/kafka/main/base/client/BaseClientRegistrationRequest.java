package org.d3javu.backend.kafka.main.base.client;

import java.time.LocalDate;

public record BaseClientRegistrationRequest(
        String id,
        String surname,
        String name,
        String patronymic,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email
) {}
