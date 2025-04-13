package org.d3javu.backend.dto.requests.card;

public record CardRenameRequest(
        Long accountId,
        Long cardId,
        String newName
) {}
