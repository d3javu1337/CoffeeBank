package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.account.CompactAccountReadDto;
import org.d3javu.backend.kafka.requests.account.AccountCreateRequest;
import org.d3javu.backend.kafka.requests.account.AccountRenameRequest;
import org.d3javu.backend.repository.AccountRepository;
import org.d3javu.backend.utils.SecurityUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientService clientService;
    private final SecurityUtil securityUtil;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Optional<CompactAccountReadDto> getAccount(Long clientId) {
        return this.accountRepository.findAccount(clientId);
    }

    @Async
    public void createAccount(String email) {
        this.kafkaTemplate.send(
                "account-create-topic",
                new AccountCreateRequest(this.clientService.getClientIdByEmail(email), email));
    }

    @Async
    public void renameAccount(String email,Long accountId, String name) {
        this.kafkaTemplate.send("account-rename-topic", new AccountRenameRequest
                (this.clientService.getClientIdByEmail(email), email, accountId, name));
    }

    public Boolean isClientOwnsAccount(Long clientId, Long accountId) {
        return this.accountRepository.isClientOwnsAccount(clientId, accountId);
    }

    public Long getAccountIdByClientId(Long clientId) {
        return this.accountRepository.findAccountIdByClientId(clientId);
    }
}