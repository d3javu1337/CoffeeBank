package org.d3javu.backend.kafka.main.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.kafka.main.business.client.BusinessClientRegistrationRequest;
import org.d3javu.backend.kafka.main.business.paymentaccount.PaymentAccountCreateRequest;
import org.d3javu.backend.services.business.BusinessClientService;
import org.d3javu.backend.services.business.PaymentAccountService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class BusinessMainKafkaService {

    private final BusinessClientService businessClientService;
    private final PaymentAccountService paymentAccountService;

    @KafkaListener(topics = {"business-client_registration_topic"}, containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.business.client.BusinessClientRegistrationRequest"},
            groupId = "main-core-consumers")
    public void businessClientRegistrationHandler(BusinessClientRegistrationRequest request) {
        System.out.println(request.officialName());
        this.businessClientService.registration(request);
    }

    @KafkaListener(topics = {"payment_account_create_topic"}, containerFactory = "mainKafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=org.d3javu.backend.kafka.main.business.paymentaccount.PaymentAccountCreateRequest"},
            groupId = "main-core-consumers")
    public void paymentAccountCreateHandler(PaymentAccountCreateRequest request) {
        this.paymentAccountService.createPaymentAccount(request);
    }

}
