package org.d3javu.backend.services.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.kafka.main.business.contactperson.ContactPersonCreateRequest;
import org.d3javu.backend.kafka.main.business.contactperson.ContactPersonDeleteRequest;
import org.d3javu.backend.kafka.main.business.contactperson.ContactPersonUpdateRequest;
import org.d3javu.backend.repository.business.ContactPersonRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactPersonService {

    private final ContactPersonRepository contactPersonRepository;
    private final BusinessClientService businessClientService;

    public void create(ContactPersonCreateRequest request) {
        this.businessClientService
                .getIdByEmail(request.businessClientEmail())
                .ifPresentOrElse(id -> this.contactPersonRepository.create(
                        id,
                        request.surname(),
                        request.name(),
                        request.patronymic(),
                        request.phoneNumber(),
                        request.email()
                ), () -> {
                    log.warn("not found id for business client {}", request.businessClientEmail());
                });
    }

    public void update(ContactPersonUpdateRequest request) {
        this.businessClientService
                .getIdByEmail(request.businessClientEmail())
                .ifPresentOrElse(id -> {try {
                    this.contactPersonRepository.update(
                    id,
                    request.contactPersonId(),
                    request.surname(),
                    request.name(),
                    request.patronymic(),
                    request.phoneNumber(),
                    request.email());
                } catch (Exception e) {
                    log.error("error {}", e);
                }
        }, () -> log.warn("not found id for business client {}", request.businessClientEmail()));
    }

    public void delete(ContactPersonDeleteRequest request) {
        this.businessClientService
                .getIdByEmail(request.businessClientEmail())
                .ifPresentOrElse(id -> this.contactPersonRepository.delete(
                        id,
                        request.personId()
                ), () -> log.warn("not found id for business client {}", request.businessClientEmail()));
    }
}
