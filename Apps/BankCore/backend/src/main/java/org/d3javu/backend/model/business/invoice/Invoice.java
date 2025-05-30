package org.d3javu.backend.model.business.invoice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.d3javu.backend.model.business.paymentaccount.PaymentAccount;

import java.util.UUID;

@Entity
@Table(name = "invoice")
@Getter
@Setter
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "payment_account_id")
    private PaymentAccount providerPaymentAccount;

}
