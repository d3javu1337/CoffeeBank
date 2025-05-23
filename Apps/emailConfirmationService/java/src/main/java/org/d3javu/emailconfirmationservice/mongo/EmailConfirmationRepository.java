package org.d3javu.emailconfirmationservice.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmailConfirmationRepository extends MongoRepository<EmailConfirmationDocument, String> {
    Optional<EmailConfirmationDocument> findByToken(String token);
}
