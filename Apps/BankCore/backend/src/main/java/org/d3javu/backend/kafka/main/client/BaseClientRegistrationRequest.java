package org.d3javu.backend.kafka.main.client;

import java.time.LocalDate;

public record BaseClientRegistrationRequest(
        String surname,
        String name,
        String patronymic,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email,
        String passwordHash
) {}
