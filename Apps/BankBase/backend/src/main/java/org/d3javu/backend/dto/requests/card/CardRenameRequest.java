package org.d3javu.backend.dto.requests.card;

public record CardRenameRequest(
        Long cardId,
        String newName
) {}
