package org.d3javu.backend.repository.base;

import org.d3javu.backend.model.base.card.Card;
import org.d3javu.backend.model.base.card.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Transactional(isolation = Isolation.SERIALIZABLE)
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query(value = "insert into card (name, type, number, expiration_date, account_id, security_code) " +
            "values (:name, :type, :number, :expirationDate, :accountId, :securityCode) " +
            "returning id",
            nativeQuery = true)
    Long initCard(String name, String type, String number, LocalDate expirationDate, Long accountId, String securityCode);

    @Modifying
    @Query(value = "update card set number= :number where id= :cardId",
            nativeQuery = true)
    void updateCardAfterInit(Long cardId, String number);

    @Modifying
    @Query(value = "update card set name= :newName where id= :cardId",
            nativeQuery = true)
    void renameCard(Long cardId, String newName);

}
