package org.d3javu.backend.model.card;


import lombok.Getter;
import lombok.Setter;
import org.d3javu.backend.model.account.PersonalAccount;

public class CardNumber {
    // a => 0-9
    // 9999 aaaa aaaa aaaa

    public CardNumber(PersonalAccount account) {
        //number generation logic with luhn alg
    }
}
