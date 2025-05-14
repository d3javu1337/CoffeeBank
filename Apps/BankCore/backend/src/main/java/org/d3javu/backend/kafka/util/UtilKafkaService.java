package org.d3javu.backend.kafka.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.services.BaseClientService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class UtilKafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BaseClientService baseClientService;

    @Async
    public void sendRequestToConfirmEmail(String email) {
        this.kafkaTemplate.send("client_email-confirmation-topic", email);
    }

    @Async
    @KafkaListener(topics = "client_confirmation_email_response-topic",
            containerFactory = "utilKafkaListenerContainerFactory", groupId = "util-core-consumers",
            properties = {}
    )
    public void confirmEmailHandler(String email) {
        log.info("Confirm email handler: " + email);
        this.baseClientService.confirmEmail(email);
    }

}
