package org.d3javu.backend.dto.requests.card;

public record AccountIdAndCardIdRequest(
        Long accountId, Long cardId
) {}
