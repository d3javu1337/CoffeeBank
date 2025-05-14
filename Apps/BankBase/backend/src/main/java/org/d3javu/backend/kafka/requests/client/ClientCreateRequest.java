package org.d3javu.backend.kafka.requests.client;

import org.d3javu.backend.dto.client.ClientCreateRecord;

import java.time.LocalDate;

public record ClientCreateRequest(String surname, String name, String patronymic, LocalDate dateOfBirth,
                                  String phoneNumber, String email, String passwordHash) {

    public ClientCreateRequest(ClientCreateRecord clientCreateRecord, String passwordHash) {
        this(
                clientCreateRecord.surname(),
                clientCreateRecord.name(),
                clientCreateRecord.patronymic(),
                clientCreateRecord.dateOfBirth(),
                clientCreateRecord.phoneNumber(),
                clientCreateRecord.email(),
                passwordHash
        );
    }
}
