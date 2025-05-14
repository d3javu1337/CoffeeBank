package org.d3javu.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.kafka.main.card.CardCreateRequest;
import org.d3javu.backend.kafka.main.card.CardRenameRequest;
import org.d3javu.backend.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardService {

    private final CoreSecurityUtilService coreSecurityUtilService;
    private final AccountService accountService;
    private final CardRepository cardRepository;


    public void createCard(CardCreateRequest cardCreateRequest) {
        if(!this.accountService.checkOwning(
                cardCreateRequest.accountId(),
                cardCreateRequest.clientId(),
                cardCreateRequest.email()
        )) log.warn("Unauthorized card creation attempt. Mismatched account and client. {}", cardCreateRequest);
        else{
            var id = this.cardRepository.initCard(
                    cardCreateRequest.name(),
                    cardCreateRequest.type(),
                    "-1",
                    LocalDate.now().plusYears(10),
                    cardCreateRequest.accountId(),
                    this.coreSecurityUtilService.generateSecurityCode()
            );
            if(id > 10000000) log.warn("Id of card creation exceeded 10000000. Take care about overload");
            var cardNumber = this.coreSecurityUtilService.generateCardNumber(
                    id,
                    cardCreateRequest.type()
            );
            this.cardRepository.updateCardAfterInit(id, cardNumber);
        }
    }

    public void renameCard(CardRenameRequest cardRenameRequest) {
        if(!this.accountService.checkOwning(
                cardRenameRequest.accountId(),
                cardRenameRequest.clientId(),
                cardRenameRequest.email()
        )) log.warn("Unauthorized card rename attempt. Mismatched account and client. {}", cardRenameRequest);
        else this.cardRepository.renameCard(cardRenameRequest.cardId(), cardRenameRequest.newName());
    }
}

