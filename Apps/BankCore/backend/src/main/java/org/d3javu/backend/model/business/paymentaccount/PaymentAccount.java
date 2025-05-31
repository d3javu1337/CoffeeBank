package org.d3javu.backend.model.business.paymentaccount;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.d3javu.backend.model.business.client.BusinessClient;

@Entity
@Table(name="payment_account")
@Getter
@Setter
@NoArgsConstructor
public class PaymentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "deposit")
    private Double deposit;

    @OneToOne
    @JoinColumn(name = "business_client_id")
    private BusinessClient client;

}
