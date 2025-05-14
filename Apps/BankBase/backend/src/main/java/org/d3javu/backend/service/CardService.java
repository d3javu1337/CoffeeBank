package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.card.CompactCardReadDto;
import org.d3javu.backend.dto.requests.account.AccountIdRequest;
import org.d3javu.backend.dto.requests.card.AccountIdAndCardIdRequest;
import org.d3javu.backend.dto.requests.card.CardRenameRequest;
import org.d3javu.backend.kafka.requests.card.CardCreateRequest;
import org.d3javu.backend.repository.CardRepository;
import org.d3javu.backend.utils.SecurityUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final SecurityUtil securityUtil;
    private final AccountService accountService;

    public List<CompactCardReadDto> getCardsByAccountId(Long accountId){
        return this.cardRepository.getCompactCardsByAccountId(accountId);
    }

    public CompactCardReadDto getCardById(AccountIdAndCardIdRequest accountIdAndCardId){
        if (!this.accountService.isClientOwnsAccount(this.securityUtil.getClientId(), accountIdAndCardId.accountId())) return null;
        return this.cardRepository.getCompactCardById(accountIdAndCardId.accountId(), accountIdAndCardId.cardId())
                .orElse(null);

    }

    @Async
    public void createCard(String email, org.d3javu.backend.dto.requests.card.CardCreateRequest cardCreateRequest){
        var clientId = this.securityUtil.getClientId(email);
        if(!this.accountService.isClientOwnsAccount(clientId, cardCreateRequest.accountId())) return;
        this.kafkaTemplate.send("card-create-topic",
                new CardCreateRequest(
                        clientId,
                        email,
                        cardCreateRequest.accountId(),
                        cardCreateRequest.type(),
                        cardCreateRequest.type().getDefaultCardName()));
    }

    @Async
    public void renameCard(String email, CardRenameRequest cardRenameRequest){
        var clientId = this.securityUtil.getClientId(email);
        if (!this.accountService.isClientOwnsAccount(clientId, cardRenameRequest.accountId())
                || !this.cardRepository.isCardBelongToAccount(cardRenameRequest.cardId(), cardRenameRequest.accountId())) return;
        this.kafkaTemplate.send("card-rename-topic", new org.d3javu.backend.kafka.requests.card.CardRenameRequest(
                this.securityUtil.getClientId(email),
                email,
                cardRenameRequest.accountId(),
                cardRenameRequest.cardId(),
                cardRenameRequest.newName()
        ));
    }

}
