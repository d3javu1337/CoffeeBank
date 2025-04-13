package org.d3javu.backend.kafka.requests.account;

public record AccountRenameRequest(
        Long clientId,
        String email,
        Long accountId,
        String newName
) {}
