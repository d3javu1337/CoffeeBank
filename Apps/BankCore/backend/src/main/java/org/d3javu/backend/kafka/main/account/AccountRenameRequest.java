package org.d3javu.backend.kafka.main.account;

public record AccountRenameRequest(
        Long clientId,
        String email,
        Long accountId,
        String newName
) {}
