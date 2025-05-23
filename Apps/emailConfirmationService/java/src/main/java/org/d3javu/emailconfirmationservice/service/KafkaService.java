package org.d3javu.emailconfirmationservice.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EmailConfirmationService emailConfirmationService;

    public KafkaService(@Lazy KafkaTemplate<String, String> kafkaTemplate, @Lazy EmailConfirmationService emailConfirmationService) {
        this.kafkaTemplate = kafkaTemplate;
        this.emailConfirmationService = emailConfirmationService;
    }

    @KafkaListener(topics = {"client_email-confirmation-topic"})
    public void confirmationRequestHandler(String email) {
        this.emailConfirmationService.createConfirmationRecord(email);
    }

    public void confirm(String email) {
        this.kafkaTemplate.send("client_confirmation_email_response-topic", email);
    }

}
