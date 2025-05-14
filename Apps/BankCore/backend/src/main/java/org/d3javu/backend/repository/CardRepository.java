package org.d3javu.backend.repository;

import org.d3javu.backend.model.card.Card;
import org.d3javu.backend.model.card.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Transactional(readOnly = true)
public interface CardRepository extends JpaRepository<Card, Long> {

    @Transactional
    @Query(value = "insert into card (name, type, number, expiration_date, account_id, security_code) " +
            "values (:name, :type, :number, :expirationDate, :accountId, :securityCode) " +
            "returning id",
            nativeQuery = true)
    Long initCard(String name, CardType type, String number, LocalDate expirationDate, Long accountId, String securityCode);

    @Modifying
    @Transactional
    @Query(value = "update card set number= :cardNumber where id= :cardId",
            nativeQuery = true)
    void updateCardAfterInit(Long cardId, String number);

    @Modifying
    @Transactional
    @Query(value = "update card set name= :newName where id= :cardId",
            nativeQuery = true)
    void renameCard(Long cardId, String newName);

}
