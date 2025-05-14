package org.d3javu.backend.kafka.main.card;

import org.d3javu.backend.model.account.AccountType;
import org.d3javu.backend.model.card.CardType;

public record CardCreateRequest(
        Long clientId,
        String email,
        Long accountId,
        CardType type,
        String name
) {}
