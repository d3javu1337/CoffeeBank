package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.account.CompactAccountReadDto;
import org.d3javu.backend.kafka.requests.account.AccountCreateRequest;
import org.d3javu.backend.kafka.requests.account.AccountRenameRequest;
import org.d3javu.backend.repository.AccountRepository;
import org.d3javu.backend.utils.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientService clientService;
    private final SecurityUtil securityUtil;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public List<CompactAccountReadDto> getAccounts() {
        return this.accountRepository.findAllAccountsByClientId
                (this.clientService.getClientIdByEmail
                        (this.securityUtil.getClientEmail()));
    }

    public Optional<CompactAccountReadDto> getAccount(Long accountId, Long clientId) {
        return this.accountRepository.findAccountById(accountId, clientId);
    }

    @Async
    public void createAccount(String email) {
        var t = this.kafkaTemplate.send(
                "account-create-topic",
                0,
                Instant.now().toEpochMilli(),
                "test",
                new AccountCreateRequest(this.clientService.getClientIdByEmail(email), email));
        try {
            System.out.println("--------------------------------------------------");
            System.out.println(t.get().getProducerRecord());
            System.out.println("--------------------------------------------------");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void renameAccount(String email,Long accountId, String name) {
        this.kafkaTemplate.send("account-rename-topic", new AccountRenameRequest
                (this.clientService.getClientIdByEmail(email), email, accountId, name));
    }

    public Boolean isClientOwnsAccount(Long clientId, Long accountId) {
        return this.accountRepository.isClientOwnsAccount(clientId, accountId);
    }

    //    public void closeAccount(Long accountId){}
}