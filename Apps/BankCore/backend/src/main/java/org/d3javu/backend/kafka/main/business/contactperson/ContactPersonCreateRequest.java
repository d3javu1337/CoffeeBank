package org.d3javu.backend.kafka.main.business.contactperson;

public record ContactPersonCreateRequest(
        String businessClientEmail,
        String surname,
        String name,
        String patronymic,
        String phoneNumber,
        String email
) {
}
