package org.d3javu.backend.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ClientCreateRecord(
        @Schema(description = "surname of user", example = "Ivanov") String surname,
        @Schema(description = "name of user", example = "Ivan") String name,
        @Schema(description = "patronymic of user", example = "Ivanovich") String patronymic,
        @Schema(description = "user`s date of birth", example = "2000-08-13") LocalDate dateOfBirth,
        @Schema(description = "user`s phone number", example = "88005553535") String phoneNumber,
        @Schema(description = "user`s email", example = "vanya@mail.ru") String email,
        @Schema(description = "user`s password", example = "jkg419flk~DY@") String password
        ) { }