package org.d3javu.backend.services.business;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.kafka.main.business.client.BusinessClientRegistrationRequest;
import org.d3javu.backend.repository.business.BusinessClientRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessClientService {

    private final BusinessClientRepository businessClientRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Async
    public void registration(BusinessClientRegistrationRequest request) {
        if (this.businessClientRepository.isClientExist(request.id())){
            this.businessClientRepository.registration(
                    request.id(),
                    request.officialName(),
                    request.brand(),
                    request.email()
            );
        }
        kafkaTemplate.send("business_registration_response_topic", request.id(), request.id());
    }
}
