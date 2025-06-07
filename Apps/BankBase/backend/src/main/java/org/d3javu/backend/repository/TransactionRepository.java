package org.d3javu.backend.repository;

import org.d3javu.backend.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "select count(*)=1 from invoice i where i.id= :invoiceNumber",
            nativeQuery = true)
    Boolean existsInvoice(UUID invoiceNumber);

    @Query(value = "select count(*)<1 from payment p where p.invoice_id= :invoiceNumber",
            nativeQuery = true)
    Boolean invoiceIsNotPayed(UUID invoiceNumber);

    @Query(value = "select i.amount from invoice i where i.id= :invoiceNumber",
            nativeQuery = true)
    Double getInvoiceAmount(UUID invoiceNumber);

}
