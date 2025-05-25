package org.d3javu.backend.repository;

import org.d3javu.backend.model.transaction.Transaction;
import org.d3javu.backend.model.transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = """
                    insert into transaction(from_id, to_id, money, type, is_completed, commited_at)  values
                    (:senderAccountId, :recipientAccountId, :money, :transactionType, :isCompleted, now()) returning id
                    """,
            nativeQuery = true)
    Long createTransaction(Long senderAccountId, Long recipientAccountId, Double money, TransactionType transactionType, Boolean isCompleted);

    @Modifying
    @Query(value = "update account set deposit = deposit - :money where id = :senderAccountId",
            nativeQuery = true)
    Integer takeMoneyFromSender(Long senderAccountId, Double money);

    @Modifying
    @Query(value = "update account set deposit = deposit + :money where id = :recipientAccountId",
            nativeQuery = true)
    Integer sendMoneyToRecipient(Long recipientAccountId, Double money);

}
