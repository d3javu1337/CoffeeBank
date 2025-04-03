package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.account.CompactAccountReadDto;
import org.d3javu.backend.repository.AccountRepository;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientService clientService;

    public List<CompactAccountReadDto> getAccountByClientEmail(String email) {
        return this.accountRepository.findAllAccountsByLinkedClientId(this.clientService.getClientIdByEmail(email));
    }

}