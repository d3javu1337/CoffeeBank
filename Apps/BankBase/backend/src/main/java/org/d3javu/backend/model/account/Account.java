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
@Table(name = "personal_account")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name = "Персональный счёт";

    @Column(name = "deposit", nullable = false, scale = 2)
    private Double deposit = 0d;

    @OneToMany(mappedBy = "linkedAccount", cascade = CascadeType.ALL)
    private List<Card> linkedCards = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client linkedClient;

    @Column(name = "type", length = 10)
    @Enumerated(EnumType.STRING)
    private AccountType accountType = AccountType.PERSONAL;

}
