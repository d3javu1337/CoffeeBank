package org.d3javu.backend.model.transaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
//@Table(name = "transaction_details")
@NoArgsConstructor
@Getter
@Setter
public class TransactionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TransactionType type;

    @PrimaryKeyJoinColumn
    @OneToOne
    private Transaction transaction;

    public TransactionDetails(TransactionType type, Transaction transaction) {
        this.type = type;
        this.transaction = transaction;
    }

}
