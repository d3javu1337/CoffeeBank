package org.d3javu.backend.services.base;

import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.kafka.main.base.client.BaseClientRegistrationRequest;
import org.d3javu.backend.kafka.util.UtilKafkaService;
import org.d3javu.backend.repository.base.BaseClientRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Optional<Long> getClientIdByPhoneNumber(String phoneNumber) {
        return this.baseClientRepository.findClientByPhoneNumber(phoneNumber);
    }

    public void confirmEmail(String email) {
        this.baseClientRepository.confirmEmail(email);
    }

}
