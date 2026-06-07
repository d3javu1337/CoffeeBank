package org.d3javu.backend.kafka.main.business.contactperson;

public record ContactPersonUpdateRequest(
        String businessClientEmail,
        Long contactPersonId,
        String surname,
        String name,
        String patronymic,
        String phoneNumber,
        String email
) {
}
