package org.d3javu.backend.kafka.main.base.card;

public record CardRenameRequest(
        Long clientId,
        String email,
        Long accountId,
        Long cardId,
        String newName
) {}
