package org.d3javu.backend.repository.business;

import org.d3javu.backend.model.business.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    @Query(value = "select count(*)=1 from invoice i where i.id= :id",
            nativeQuery = true)
    Boolean existsInvoiceById(UUID id);

    @Query(value = "select i.amount from invoice i where i.id= :id",
            nativeQuery = true)
    Double getInvoiceAmountById(UUID id);

    @Query(value = "select i.provider_payment_account_id from invoice i where i.id= :id",
            nativeQuery = true)
    Long getInvoiceProviderPaymentAccountIdById(UUID id);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Query(value = "insert into invoice(id, amount, provider_payment_account_id) " +
            "values(gen_random_uuid(), :amount, :providerPaymentAccountId) returning id",
            nativeQuery = true)
    UUID createInvoice(Long providerPaymentAccountId, Double amount);

}
