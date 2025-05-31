package org.d3javu.backend.model.business.payment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.d3javu.backend.model.base.personalaccount.PersonalAccount;
import org.d3javu.backend.model.business.invoice.Invoice;
import org.d3javu.backend.model.business.paymentaccount.PaymentAccount;
import org.d3javu.backend.model.transaction.Transaction;

import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "payment_account_id")
    private PaymentAccount paymentAccount;

    @OneToOne
    @JoinColumn(name = "personal_account_id")
    private PersonalAccount personalAccount;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transactionId;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

}
