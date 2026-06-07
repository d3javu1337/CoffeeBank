package org.d3javu.backend.kafka.main.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.kafka.main.business.client.BusinessClientRegistrationRequest;
import org.d3javu.backend.kafka.main.business.contactperson.ContactPersonCreateRequest;
import org.d3javu.backend.kafka.main.business.contactperson.ContactPersonDeleteRequest;
import org.d3javu.backend.kafka.main.business.contactperson.ContactPersonUpdateRequest;
import org.d3javu.backend.kafka.main.business.paymentaccount.PaymentAccountCreateRequest;
import org.d3javu.backend.services.business.BusinessClientService;
import org.d3javu.backend.services.business.ContactPersonService;
import org.d3javu.backend.services.business.PaymentAccountService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class BusinessMainKafkaService {

    private final BusinessClientService businessClientService;
    private final PaymentAccountService paymentAccountService;
    private final ContactPersonService contactPersonService;

    @KafkaListener(topics = {"business-client_registration_topic"}, containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.business.client.BusinessClientRegistrationRequest"},
            groupId = "main-core-consumers")
    public void businessClientRegistrationHandler(BusinessClientRegistrationRequest request) {
        System.out.println(request.officialName());
        this.businessClientService.registration(request);
    }

    @KafkaListener(topics = {"payment-account_create_topic"}, containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.business.paymentaccount.PaymentAccountCreateRequest"},
            groupId = "main-core-consumers")
    public void paymentAccountCreateHandler(PaymentAccountCreateRequest request) {
        this.paymentAccountService.createPaymentAccount(request);
    }

    @KafkaListener(topics = {"contact-person_create_topic"}, containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.business.contactperson.ContactPersonCreateRequest"},
            groupId = "main-core-consumers")
    public void contactPersonCreateHandler(ContactPersonCreateRequest request) {
        log.info("consumed {}", request.email());
        contactPersonService.create(request);
    }

    @KafkaListener(topics = {"contact-person_update_topic"}, containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.business.contactperson.ContactPersonUpdateRequest"},
            groupId = "main-core-consumers")
    public void contactPersonUpdateHandler(ContactPersonUpdateRequest request) {
        log.info("consumed {}", request.businessClientEmail());
        log.info("consumed {}", request.name());
        log.info("consumed {}", request.contactPersonId());
        contactPersonService.update(request);
    }

    @KafkaListener(topics = {"contact-person_delete_topic"}, containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.business.contactperson.ContactPersonDeleteRequest"},
            groupId = "main-core-consumers")
    public void contactPersonDeleteHandler(ContactPersonDeleteRequest request) {
        contactPersonService.delete(request);
    }

}
