package org.d3javu.backend.repository.business;

import jakarta.transaction.Transactional;
import org.d3javu.backend.model.business.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

@Transactional
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Modifying
    @Query(value = "update personal_account set deposit = deposit - :amount where id = :payerAccountId",
            nativeQuery = true)
    Integer takeMoneyFromPayer(Long payerAccountId, Double amount);

    @Modifying
    @Query(value = "update payment_account set deposit = deposit + :amount where id = :recipientAccountId",
            nativeQuery = true)
    Integer sendMoneyToRecipient(Long recipientAccountId, Double amount);

    @Query(value = "insert into payment(id, payment_account_id, personal_account_id, transaction_id) " +
            "values (gen_random_uuid(), :providerPaymentAccountId, :payerPersonalAccountId, :transactionId) returning id",
            nativeQuery = true)
    UUID createPayment(Long providerPaymentAccountId, Long payerPersonalAccountId, UUID transactionId);

}
