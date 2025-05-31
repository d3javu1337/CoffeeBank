package org.d3javu.backend.services.business;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.kafka.main.business.client.BusinessClientRegistrationRequest;
import org.d3javu.backend.repository.business.BusinessClientRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessClientService {

    private final BusinessClientRepository businessClientRepository;

    @Async
    public void registration(BusinessClientRegistrationRequest request) {
        this.businessClientRepository.registration(
                request.officialName(),
                request.brand(),
                request.email(),
                request.passwordHash()
        );
    }
}
