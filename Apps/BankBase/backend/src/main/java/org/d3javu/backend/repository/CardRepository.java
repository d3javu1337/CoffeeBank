package org.d3javu.backend.repository;

import org.d3javu.backend.model.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
