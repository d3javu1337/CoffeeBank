package org.d3javu.backend.dto.requests.transaction;

public record TransferByPhoneNumberRequest(
        String phoneNumber,
        Double amount
) {}
