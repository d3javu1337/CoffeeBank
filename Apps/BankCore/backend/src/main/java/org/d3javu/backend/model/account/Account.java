package org.d3javu.backend.model.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.d3javu.backend.model.card.Card;
import org.d3javu.backend.model.client.Client;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_name", nullable = false, length = 50)
    private String accountName = "Персональный счёт";

    @Column(name = "deposit", nullable = false, scale = 2)
    private Double accountDeposit = 0d;

    @OneToMany(mappedBy = "linkedAccount", cascade = CascadeType.ALL)
    private List<Card> linkedCards = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "client_id")
//    private Client linkedClient;
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client linkedClient;

    @Column(name = "type", length = 10)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

}
