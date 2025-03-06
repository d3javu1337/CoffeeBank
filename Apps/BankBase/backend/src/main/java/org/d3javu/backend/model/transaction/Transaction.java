package org.d3javu.backend.model.transaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.d3javu.backend.model.account.PersonalAccount;
import org.d3javu.backend.model.cheque.Cheque;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "account1_id")
    private PersonalAccount account1;

    @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "account2_id")
    private PersonalAccount account2;

    @Column(name = "money", nullable = false, scale = 2)
    private double money;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ETransaction type;

//    @Column(name = "details", nullable = false)
//    private String details;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    private Cheque cheque;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    public Transaction() {}

//    public Transaction(PersonalAccount account1, @Nullable PersonalAccount account2, @Nullable POS_Terminal posTerminal, @Nullable ATM atm, ETransaction type, double money) {
//        this.account1 = account1;
//        this.account2 = account2;
//        this.paymentAccount = posTerminal == null ? null : posTerminal.getLinkedPaymentAccount();
//        this.atm = atm;
//        this.type = type;
//        this.money = money;
//        this.completed = switch (type){
//            case PURCHASE -> this.purchase();
//            case TRANSFER -> this.transfer();
//            case WITHDRAW -> this.withdraw();
//            case REPLENISH -> this.replenish();
//        };
//        this.cheque = new Cheque(this);
//        this.creationDate = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
//    }

    /*
    private boolean withdraw() {

        var money = this.money;

        if (!this.atm.isEnoughMoney(money) || !this.account1.isEnoughDeposit(money)) {
            this.account1.addLinkedTransaction(this);
            this.atm.addLinkedTransaction(this);
            return false;
        }

        this.account1.setAccountDeposit(this.account1.getAccountDeposit() - money);
        this.atm.setMoneyAmount(this.atm.getMoneyAmount() - money);

        this.account1.addLinkedTransaction(this);
        this.atm.addLinkedTransaction(this);

        return true;
    } //снять
    private boolean replenish() {

        var money = this.money;

        this.account1.setAccountDeposit(this.account1.getAccountDeposit() + money);
        this.atm.setMoneyAmount(this.atm.getMoneyAmount() + money);

        this.account1.addLinkedTransaction(this);
        this.atm.addLinkedTransaction(this);

        return true;
    } //пополнить
    private boolean transfer() {

        var money = this.money;

        if(!this.account1.isEnoughDeposit(money)) {
            this.account1.addLinkedTransaction(this);
            this.account2.addLinkedTransaction(this);
            return false;
        }

        this.account1.setAccountDeposit(this.account1.getAccountDeposit() - money);
        this.account2.setAccountDeposit(this.account2.getAccountDeposit() + money);

        this.account1.addLinkedTransaction(this);
        this.account2.addLinkedTransaction(this);

        return true;
    }
    private boolean purchase() {

        var money = this.money;

        if(!this.account1.isEnoughDeposit(money)) {
            this.account1.addLinkedTransaction(this);
            this.paymentAccount.addLinkedTransaction(this);
            return false;
        }

        this.account1.setAccountDeposit(this.account1.getAccountDeposit() - money);
        this.paymentAccount.setDeposit(this.paymentAccount.getDeposit() + money);

        this.account1.addLinkedTransaction(this);
        this.paymentAccount.addLinkedTransaction(this);

        return true;
    }

    */


//    public Transaction(BankClient client1, BankClient client2, double money){
//        this.Client = client1;
//        this.PersonalAccount = this.Client.getPersonalAccounts().get(0);
//        this.OtherClient = client2;
//        this.OtherPersonalAccount = this.OtherClient.getPersonalAccounts().get(0);
//        this.Money = money;
//        this.TransactionType = ETransaction.CLIENT2CLIENT;
//        this.NumberOfTransaction = new TransactionNumber();
//    }
//
//    public Transaction(BankClient client1, PersonalAccount account1, BankClient client2, PersonalAccount account2, double money){
//        this.Client = client1;
//        this.OtherClient = client2;
//        this.PersonalAccount = account1;
//        this.OtherPersonalAccount = account2;
//        this.Money = money;
//        this.TransactionType = ETransaction.CLIENT2CLIENT;
//        this.NumberOfTransaction = new TransactionNumber();
//    }
//
//    public Transaction(ATM atm, BankClient client, PersonalAccount acc, double money){
//        this.Atm = atm;
//        this.user1 = atm;
//        this.Client = client;
//        this.PersonalAccount = acc;
//        this.Money = money;
//        this.TransactionType = ETransaction.ATM2CLIENT;
//        this.NumberOfTransaction = new TransactionNumber();
//    }
//
//    public Transaction(ATM atm, BankClient client, double money){
//        this.Atm = atm;
//        this.Client = client;
//        this.PersonalAccount = client.getPersonalAccounts().get(0);
//        this.Money = money;
//        this.TransactionType = ETransaction.ATM2CLIENT;
//        this.NumberOfTransaction = new TransactionNumber();
//    }
//
//    public Transaction(POS_Terminal terminal, BankCard card, double money){
//        this.Terminal = terminal;
//        this.Card = card;
//        this.Money = money;
//        this.TransactionType = ETransaction.CLIENT2POS;
//        this.NumberOfTransaction = new TransactionNumber();
//    }
//
//////////////////////////////////////////////////////////////////////
//
//    public void TransactionTransfer(){
//
//        var cl1Index = this.Client.getPersonalAccounts().indexOf(this.PersonalAccount);
//        var cl1Acc = this.Client.getPersonalAccounts().get(cl1Index);
//
//        var cl2Index = this.OtherClient.getPersonalAccounts().indexOf(this.OtherPersonalAccount);
//        var cl2Acc = this.OtherClient.getPersonalAccounts().get(cl2Index);
//
//        if(cl1Acc.getAccountDeposit() >= Money){
//
//            cl1Acc.setAccountDeposit(cl1Acc.getAccountDeposit() - Money);
//            cl2Acc.setAccountDeposit(cl2Acc.getAccountDeposit() + Money);
//
//            this.Client.getPersonalAccounts().set(cl1Index, cl1Acc);
//            this.OtherClient.getPersonalAccounts().set(cl2Index, cl2Acc);
//            System.out.println("Transfer succeed!");
//        }else{
//            System.out.println("you aren`t have enough money");
//        }
//    }
//
//
//    public void TransactionTransferByCardNumber(){}
//
//
//    public void TransactionWithdraw(){
//        if(this.Atm.getMoneyAmount() >= this.Money){
//            var index = this.Client.getPersonalAccounts().indexOf(this.PersonalAccount);
//            var acc = this.Client.getPersonalAccounts().get(index);
//            if(acc.getAccountDeposit() >= this.Money){
//                acc.setAccountDeposit(acc.getAccountDeposit() - this.Money);
//                this.Atm.setMoneyAmount(this.Atm.getMoneyAmount() - this.Money);
//                this.Atm.giveMoney();
//            }else{
//                System.out.println("you aren`t have enough money");
//            }
//        }else{
//            System.out.println("ATM money amount is lower than you need");
//        }
//    }
//
//
//    public void TransactionReplenish(){
//        this.PersonalAccount.setAccountDeposit(this.PersonalAccount.getAccountDeposit() + Money);
//        this.Atm.setMoneyAmount(this.Atm.getMoneyAmount() + this.Money);
//        this.Atm.takeMoney();
//    }
//
//    public void TransactionPurchase(POS_Terminal terminal, BankCard card, double cost){
//        if(card.getLinkedPersonalAccount().getAccountDeposit() >= cost){
//            card.getLinkedPersonalAccount().setAccountDeposit(card.getLinkedPersonalAccount().getAccountDeposit() - cost);
//            terminal.getLinkedPaymentAccount().setAccountDeposit(terminal.getLinkedPaymentAccount().getAccountDeposit() + cost);
//            System.out.println("purchase succeed!");
//        }else{
//            System.out.println("you aren`t have enough money");
//        }
//    }
//
}
