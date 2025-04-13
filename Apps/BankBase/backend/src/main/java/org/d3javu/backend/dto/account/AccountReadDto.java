package org.d3javu.backend.dto.account;

import org.d3javu.backend.model.account.AccountType;

public interface AccountReadDto {

    Long getId();
    String getAccountName();
    Double getAccountDeposit();
    AccountType getAccountType();

}
