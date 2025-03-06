package org.d3javu.backend.model.card;

public enum ECardType {
    credit("Кредитная карта"),
    debit("Дебетовая карта"),
    overdraft("Карта с овердрафтом"),
    prepaid("Предоплаченая карта");

    private final String CardType;

    ECardType(String defaultName){
        this.CardType = defaultName;
    }

    public String getDefaultCardName() {
        return CardType;
    }
}
