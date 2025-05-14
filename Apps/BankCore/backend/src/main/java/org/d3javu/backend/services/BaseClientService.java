package org.d3javu.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.kafka.main.client.BaseClientRegistrationRequest;
import org.d3javu.backend.kafka.util.UtilKafkaService;
import org.d3javu.backend.repository.BaseClientRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BaseClientService {

    private final BaseClientRepository baseClientRepository;
    private final UtilKafkaService utilKafkaService;

    public BaseClientService(@Lazy BaseClientRepository baseClientRepository, @Lazy UtilKafkaService utilKafkaService) {
        this.baseClientRepository = baseClientRepository;
        this.utilKafkaService = utilKafkaService;
    }

    @Async
    public void registration(BaseClientRegistrationRequest request) {
        this.baseClientRepository.registration(
                request.surname(),
                request.name(),
                request.patronymic(),
                request.dateOfBirth(),
                request.phoneNumber(),
                request.email(),
                request.passwordHash()
        );
        this.utilKafkaService.sendRequestToConfirmEmail(request.email());
    }

    public void confirmEmail(String email) {
        this.baseClientRepository.confirmEmail(email);
    }

}
