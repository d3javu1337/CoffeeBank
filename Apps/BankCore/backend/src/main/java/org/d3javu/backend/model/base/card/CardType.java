package org.d3javu.backend.model.base.card;

public enum CardType {
    credit("Кредитная карта"),
    debit("Дебетовая карта"),
    overdraft("Карта с овердрафтом"),
    prepaid("Предоплаченная карта");

    private final String CardType;

    CardType(String defaultName){
        this.CardType = defaultName;
    }

    public String getDefaultCardName() {
        return CardType;
    }
}
