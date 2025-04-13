package org.d3javu.backend.repository;

import org.d3javu.backend.dto.card.CompactCardReadDto;
import org.d3javu.backend.model.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query(value = "select c.name as name, c.type as type, c.number as number from card c where account_id= :accountId",
            nativeQuery = true)
    List<CompactCardReadDto> getCompactCardsByAccountId(Long accountId);

    @Query(value = "select c.name as name, c.type as type, c.number as number from card c where c.id= :cardId and c.account_id= :accountId",
            nativeQuery = true)
    Optional<CompactCardReadDto> getCompactCardById(Long accountId, Long cardId);

    @Query(value = "select count(*) > 0 from card c where c.id= :cardId and c.account_id= :accountId",
            nativeQuery = true)
    Boolean isCardBelongToAccount(Long cardId, Long accountId);
}
