package org.d3javu.backend.services.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.kafka.main.base.personalaccount.PersonalAccountCreateRequest;
import org.d3javu.backend.kafka.main.base.personalaccount.PersonalAccountRenameRequest;
import org.d3javu.backend.model.base.personalaccount.AccountType;
import org.d3javu.backend.repository.base.PersonalAccountRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonalAccountService {

    private final PersonalAccountRepository personalAccountRepository;

    public void createAccount(PersonalAccountCreateRequest personalAccountCreateRequest) {
        var id = this.personalAccountRepository.createAccount(
                personalAccountCreateRequest.id(),
                AccountType.PERSONAL.name(),
                AccountType.PERSONAL.name
        );
        log.info("Created account {}", id);
    }

    public void renameAccount(PersonalAccountRenameRequest personalAccountRenameRequest) {
        if (this.checkOwning(
                personalAccountRenameRequest.accountId(),
                personalAccountRenameRequest.clientId(),
                personalAccountRenameRequest.email())) {
            this.personalAccountRepository.renameAccount(
                    personalAccountRenameRequest.accountId(),
                    personalAccountRenameRequest.newName()
            );
        }
    }

    public boolean checkOwning(Long accountId, Long clientId, String email) {
        return this.personalAccountRepository.checkOwning(accountId, clientId, email);
    }

    public Long getAccountIdByClientId(Long clientId) {
        return this.personalAccountRepository.findAccountByClientId(clientId);
    }

    public Boolean hasEnoughMoney(Long accountId, Double money) {
        return this.personalAccountRepository.hasEnoughMoney(accountId, money);
    }

}
