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

//    @Override
//    public String toString() {
//        var builder = new StringBuilder();
//        builder.append(type.ordinal());
//        builder.append("-");
//
//        switch (type) {
//            case WITHDRAW -> builder.append(this.transaction.getAtm().getId()).append(":").append(this.transaction.getAccount1().getId());
//            case REPLENISH -> builder.append(this.transaction.getAccount1().getId()).append(":").append(this.transaction.getAtm().getId());
//            case PURCHASE -> builder.append(this.transaction.getAccount1().getId()).append(":").append(this.transaction.getPaymentAccount().getId());
//            case TRANSFER -> builder.append(this.transaction.getAccount1().getId()).append(":").append(this.transaction.getAccount2().getId());
//        }
//
//        return builder.toString();
//    }
}
