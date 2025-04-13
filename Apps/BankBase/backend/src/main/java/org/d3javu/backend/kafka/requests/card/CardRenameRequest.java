package org.d3javu.backend.kafka.requests.card;

public record CardRenameRequest(
        Long clientId,
        String email,
        Long accountId,
        Long cardId,
        String newName
) {}
