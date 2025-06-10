package org.d3javu.backend.repository.business;

import org.d3javu.backend.model.business.paymentaccount.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Long> {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Query(value = "insert into payment_account(name, deposit, business_client_id) " +
            "values('Расчётный счёт', 0.0, :businessClientId)",
            nativeQuery = true)
    Long createPaymentAccount(Long businessClientId);

    @Query(value = "select count(*)=1 from payment_account p where p.business_client_id= :clientId",
            nativeQuery = true)
    Boolean existsPaymentAccountByClientId(Long clientId);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Modifying
    @Query(value = "update payment_account set invoice_create_token= :token where id= :paymentAccountId",
            nativeQuery = true)
    void createInvoiceIssuingToken(Long paymentAccountId, UUID token);
}
