package org.d3javu.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.kafka.main.account.AccountCreateRequest;
import org.d3javu.backend.kafka.main.account.AccountRenameRequest;
import org.d3javu.backend.model.account.AccountType;
import org.d3javu.backend.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public void createAccount(AccountCreateRequest accountCreateRequest) {
        var id = this.accountRepository.createAccount(
                accountCreateRequest.id(),
                AccountType.PERSONAL
        );
        log.info("Created account {}", id);
    }

    public void renameAccount(AccountRenameRequest accountRenameRequest) {
        if (this.checkOwning(
                accountRenameRequest.accountId(),
                accountRenameRequest.clientId(),
                accountRenameRequest.email())) {
            this.accountRepository.renameAccount(
                    accountRenameRequest.accountId(),
                    accountRenameRequest.newName()
            );
        }
    }

    public boolean checkOwning(Long accountId, Long clientId, String email) {
        return this.accountRepository.checkOwning(accountId, clientId, email);
    }

    public Long getAccountIdByClientId(Long clientId) {
        return this.accountRepository.findAccountByClientId(clientId);
    }

    public Boolean hasEnoughMoney(Long accountId, Double money) {
        return this.accountRepository.hasEnoughMoney(accountId, money);
    }

}
