package org.d3javu.backend.dto.client;

import java.time.LocalDate;

public record ClientCreateRecord(
        String surname,
        String name,
        String patronymic,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email
        ) { }