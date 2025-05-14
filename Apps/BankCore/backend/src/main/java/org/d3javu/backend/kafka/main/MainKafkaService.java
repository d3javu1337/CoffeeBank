package org.d3javu.backend.kafka.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.kafka.main.account.AccountCreateRequest;
import org.d3javu.backend.kafka.main.account.AccountRenameRequest;
import org.d3javu.backend.kafka.main.card.CardCreateRequest;
import org.d3javu.backend.kafka.main.card.CardRenameRequest;
import org.d3javu.backend.kafka.main.client.BaseClientRegistrationRequest;
import org.d3javu.backend.services.AccountService;
import org.d3javu.backend.services.CardService;
import org.d3javu.backend.services.BaseClientService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class MainKafkaService {

    private final AccountService accountService;
    private final CardService cardService;
    private final BaseClientService baseClientService;

    @KafkaListener(topics = "account-create-topic", containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.account.AccountCreateRequest"}, groupId = "main-core-consumers")
    public void createAccountHandler(AccountCreateRequest accountCreateRequest) {
        this.accountService.createAccount(accountCreateRequest);
    }

    @KafkaListener(topics = "account-rename-topic", containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.account.AccountRenameRequest"}, groupId = "main-core-consumers")
    public void renameAccountHandler(AccountRenameRequest accountRenameRequest) {
        this.accountService.renameAccount(accountRenameRequest);
    }

    @KafkaListener(topics = "card-create-topic", containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.card.CardCreateRequest"}, groupId = "main-core-consumers")
    public void createCardHandler(CardCreateRequest cardCreateRequest) {
        this.cardService.createCard(cardCreateRequest);
    }

    @KafkaListener(topics = "card-rename-topic", containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.card.CardRenameRequest"}, groupId = "main-core-consumers")
    public void renameCardHandler(CardRenameRequest cardRenameRequest) {
        this.cardService.renameCard(cardRenameRequest);
    }


    //hope it will match types by itself. If not -> should do some thoughts
    @KafkaListener(topics = "client-registration-topic", containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.client.BaseClientRegistrationRequest"}, groupId = "main-core-consumers")
    public void baseClientRegistrationHandler(BaseClientRegistrationRequest baseClientRegistrationRequest) {
        this.baseClientService.registration(baseClientRegistrationRequest);
    }

    @KafkaListener(topics = "client-registration-topic", containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.client.BusinessClientRegistrationRequest"}, groupId = "main-core-consumers")
    public void businessClientRegistrationHandler(){}

}
