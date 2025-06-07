package org.d3javu.backend.model.account;

public enum AccountType {
    PERSONAL("Личный счет"),
    BUSINESS("Расчетный счет");

    public final String name;

    private AccountType(String name) {
        this.name = name;
    }

}
