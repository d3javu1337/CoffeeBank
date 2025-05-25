package org.d3javu.backend.model.transaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.d3javu.backend.model.account.Account;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "from_id")
    private Account from;


    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "to_id", nullable = false)
    private Account to;


    @Column(name = "money", nullable = false, scale = 2)
    private Double money;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @Column(name = "commited_at", nullable = false)
    private LocalDateTime commitedAt;

    public Transaction(Account from, Account to, Double money, TransactionType type) {
        this.from = from;
        this.to = to;
        this.money = money;
        this.type = type;
    }
}
