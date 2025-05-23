package org.d3javu.emailconfirmationservice.service;

import lombok.RequiredArgsConstructor;
import org.d3javu.emailconfirmationservice.mongo.EmailConfirmationDocument;
import org.d3javu.emailconfirmationservice.mongo.EmailConfirmationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailConfirmationService {

    private final EmailConfirmationRepository emailConfirmationRepository;
    private final KafkaService kafkaService;

    public void createConfirmationRecord(String email) {
        this.emailConfirmationRepository.save(new EmailConfirmationDocument(email));
    }

    public Boolean confirmEmailByToken(String token) {
        final var emailConfirmationDocument = this.emailConfirmationRepository.findByToken(token);
        return emailConfirmationDocument.
                map(document -> {
                    this.kafkaService.confirm(document.getEmail());
                    this.emailConfirmationRepository.delete(document);
                    return true;
                })
                .orElse(false);
    }

}
