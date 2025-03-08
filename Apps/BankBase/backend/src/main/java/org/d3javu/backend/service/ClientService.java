package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.client.ClientCreateRecord;
import org.d3javu.backend.dto.client.CompactClientReadDto;
import org.d3javu.backend.model.account.Account;
import org.d3javu.backend.model.client.Client;
import org.d3javu.backend.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;

    public List<CompactClientReadDto> getAllClients(){
        return this.clientRepository.findAllICompactClientReadDto();
    }

    @Transactional
    public CompactClientReadDto registration(ClientCreateRecord clientCreateRecord) {
        return (CompactClientReadDto) this.clientRepository.saveAndFlush(
                new Client(
                clientCreateRecord.surname(),
                clientCreateRecord.name(),
                clientCreateRecord.patronymic(),
                clientCreateRecord.dateOfBirth(),
                clientCreateRecord.phoneNumber(),
                null,
                clientCreateRecord.email(),
                "gagagaga",
                List.of(new Account())
        ));
    }
}
