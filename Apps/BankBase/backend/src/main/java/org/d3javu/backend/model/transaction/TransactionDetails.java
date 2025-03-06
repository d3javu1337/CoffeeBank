package org.d3javu.backend.model.transaction;

public class TransactionDetails {

    private ETransaction type;
    private Transaction transaction;

    public TransactionDetails(ETransaction type, Transaction transaction) {
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
