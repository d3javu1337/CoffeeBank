package org.d3javu.backend.dto.requests.card;

import org.d3javu.backend.model.card.CardType;

public record CardCreateRequest(
        CardType type
) {}
