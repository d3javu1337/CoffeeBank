package org.d3javu.backend.kafka.main.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.kafka.main.base.personalaccount.PersonalAccountCreateRequest;
import org.d3javu.backend.kafka.main.base.personalaccount.PersonalAccountRenameRequest;
import org.d3javu.backend.kafka.main.base.card.CardCreateRequest;
import org.d3javu.backend.kafka.main.base.card.CardRenameRequest;
import org.d3javu.backend.kafka.main.base.client.BaseClientRegistrationRequest;
import org.d3javu.backend.services.base.AccountService;
import org.d3javu.backend.services.base.CardService;
import org.d3javu.backend.services.base.BaseClientService;
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
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.base.personalaccount.PersonalAccountCreateRequest"},
            groupId = "main-core-consumers")
    public void createAccountHandler(PersonalAccountCreateRequest personalAccountCreateRequest) {
        this.accountService.createAccount(personalAccountCreateRequest);
    }

    @KafkaListener(topics = "account-rename-topic", containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.base.personalaccount.PersonalAccountRenameRequest"},
            groupId = "main-core-consumers")
    public void renameAccountHandler(PersonalAccountRenameRequest personalAccountRenameRequest) {
        this.accountService.renameAccount(personalAccountRenameRequest);
    }

    @KafkaListener(topics = "card-create-topic", containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.base.card.CardCreateRequest"},
            groupId = "main-core-consumers")
    public void createCardHandler(CardCreateRequest cardCreateRequest) {
        this.cardService.createCard(cardCreateRequest);
    }

    @KafkaListener(topics = "card-rename-topic", containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.base.card.CardRenameRequest"},
            groupId = "main-core-consumers")
    public void renameCardHandler(CardRenameRequest cardRenameRequest) {
        this.cardService.renameCard(cardRenameRequest);
    }

    //hope it will match types by itself. If not -> should do some thoughts
    @KafkaListener(topics = "client-registration-topic", containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.base.client.BaseClientRegistrationRequest"},
            groupId = "main-core-consumers")
    public void baseClientRegistrationHandler(BaseClientRegistrationRequest baseClientRegistrationRequest) {
        this.baseClientService.registration(baseClientRegistrationRequest);
    }

//    @KafkaListener(topics = "client-registration-topic", containerFactory = "mainKafkaListenerContainerFactory",
//            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.client.BusinessClientRegistrationRequest"}, groupId = "main-core-consumers")
//    public void businessClientRegistrationHandler(){}

}
