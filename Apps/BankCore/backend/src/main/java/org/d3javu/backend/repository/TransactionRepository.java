package org.d3javu.backend.repository;

import org.d3javu.backend.model.transaction.Transaction;
import org.d3javu.backend.model.transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query(value = """
                    insert into transaction(id, sender_id, recipient_id, amount, type, is_completed, commited_at)  values
                    (gen_random_uuid(), :senderAccountId, :recipientAccountId, :money, :transactionType, :isCompleted, now()) returning id
                    """,
            nativeQuery = true)
    UUID createTransaction(Long senderAccountId, Long recipientAccountId, Double money, TransactionType transactionType, Boolean isCompleted);

    @Modifying
    @Query(value = "update personal_account set deposit = deposit - :amount where id = :senderAccountId",
            nativeQuery = true)
    Integer takeMoneyFromSender(Long senderAccountId, Double amount);

    @Modifying
    @Query(value = "update personal_account set deposit = deposit + :amount where id = :recipientAccountId",
            nativeQuery = true)
    Integer sendMoneyToRecipient(Long recipientAccountId, Double amount);

}
