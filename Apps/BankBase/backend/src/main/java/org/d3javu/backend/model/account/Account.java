package org.d3javu.backend.model.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.d3javu.backend.model.card.Card;
import org.d3javu.backend.model.client.Client;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_name", nullable = false, length = 50)
    private String accountName;

    @Column(name = "deposit", nullable = false, scale = 2)
    private Double accountDeposit;

    @OneToMany(mappedBy = "linkedAccount", cascade = CascadeType.ALL)
    private List<Card> linkedCards = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_client_id")
    private Client linkedClient;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EAccountType accountType;

    public Account() {
        this.accountName = "Персональный счёт";
        this.accountDeposit = 0d;
        this.accountType = EAccountType.PERSONAL;
    }

    public Account(Client client) {
        this.accountName = "Персональный счёт";
        this.accountDeposit = 0d;
        this.linkedClient = client;
        this.accountType = EAccountType.PERSONAL;
    }
}
