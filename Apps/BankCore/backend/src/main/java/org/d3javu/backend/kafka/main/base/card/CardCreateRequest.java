package org.d3javu.backend.kafka.main.base.card;

import org.d3javu.backend.model.base.card.CardType;

public record CardCreateRequest(
        Long clientId,
        String email,
        Long accountId,
        CardType cardType,
        String name
) {}
