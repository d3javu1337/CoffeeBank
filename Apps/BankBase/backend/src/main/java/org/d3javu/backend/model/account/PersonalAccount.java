package org.d3javu.backend.model.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.d3javu.backend.model.card.Card;
import org.d3javu.backend.model.card.ECardType;
import org.d3javu.backend.model.client.Client;
import org.d3javu.backend.model.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personal_account")
@Getter
@Setter
public class PersonalAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "account_name", nullable = false, length = 50)
    private String accountName;

    @Column(name = "deposit", nullable = false, scale = 2)
    private double accountDeposit;

    @OneToMany(mappedBy = "linkedPersonalAccount", cascade = CascadeType.ALL)
    private List<Card> linkedCards = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "linked_client_id")
    private Client linkedClient;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Transaction> linkedTransactions = new ArrayList<>();

    public PersonalAccount() {
        this.accountName = "Персональный счёт";
        this.accountDeposit = 0d;
    }

    public PersonalAccount(String name, Client client) {
        this.accountName = name;
        this.accountDeposit = 0d;
        this.linkedClient = client;
    }

    public PersonalAccount(Client client) {
        this.accountName = "Персональный счёт";
        this.accountDeposit = 0d;
        this.linkedClient = client;
    }

    public void openNewCard(ECardType type, String pinCode){
//        var card = new Card(type, pinCode, this);
//        this.linkedCards.add(card);
    }

//    public boolean changePersonalAccountName(String newName){
//        if(newName.length() < 50 && !newName.contains(";")){
//            AccountName = newName;
//            return true;
//        }
//        return false;
//    }
}
