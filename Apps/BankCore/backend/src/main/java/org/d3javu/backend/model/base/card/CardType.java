package org.d3javu.backend.model.base.card;

public enum CardType {
    CREDIT("Кредитная карта"),
    DEBIT("Дебетовая карта"),
    OVERDRAFT("Карта с овердрафтом"),
    PREPAID("Предоплаченная карта");

    private final String CardType;

    CardType(String defaultName){
        this.CardType = defaultName;
    }

    public String getDefaultCardName() {
        return CardType;
    }
}
