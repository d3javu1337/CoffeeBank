package org.d3javu.backend.dto.card;

import org.d3javu.backend.model.card.CardType;

public interface CompactCardReadDto {

    Long getId();
    String getName();
    CardType getType();
    String getNumber();

}
