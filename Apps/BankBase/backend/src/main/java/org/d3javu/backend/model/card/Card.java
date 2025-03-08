package org.d3javu.backend.model.card;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.d3javu.backend.model.account.Account;

import java.sql.Date;

@Entity
@Table(name = "bank_card")
@Getter
@Setter
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String cardName;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ECardType cardType;

    @Column(name = "number", nullable = false, unique = true, length = 16)
    private String cardNumber;

    @Column(name = "expiration_date", nullable = false, updatable = false)
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "linked_personal_account_id", nullable = false)
    private Account linkedAccount;

    @Column(name = "pin_hash", nullable = false)
    private String pinHash;

    @Column(name = "security_code", nullable = false, updatable = false, length = 3)
    private String securityCode;


//    public Card(ECardType cardType, String pinCode, PersonalAccount acc){
//        this.cardName = cardType.getDefaultCardName();
//        this.cardType = cardType;
//        this.cardNumber = new CardNumber(acc).getNumber();
//        this.expirationDate = Date.valueOf(LocalDate.now().plusYears(10));
//        this.linkedPersonalAccount = acc;
//        this.pinCodeHash = pinCode.matches("\\d{4}") ? Security.getHash(pinCode) : null;
//        this.code = Security.cvvGenerator(this.expirationDate);
//
//    }

}
