package org.d3javu.backend.kafka.main.business.contactperson;

public record ContactPersonDeleteRequest(
        String businessClientEmail,
        Long personId
) {
}
