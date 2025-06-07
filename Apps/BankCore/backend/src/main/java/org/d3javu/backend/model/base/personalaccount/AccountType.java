package org.d3javu.backend.model.base.personalaccount;

public enum AccountType {
    PERSONAL("Личный счёт"),
    BUSINESS("Расчетный счет");

    public final String name;

    private AccountType(String name) {
        this.name = name;
    }

}