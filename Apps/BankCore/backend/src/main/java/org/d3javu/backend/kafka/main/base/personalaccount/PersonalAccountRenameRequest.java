package org.d3javu.backend.kafka.main.base.personalaccount;

public record PersonalAccountRenameRequest(
        Long clientId,
        String email,
        Long accountId,
        String newName
) {}
