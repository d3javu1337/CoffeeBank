package org.d3javu.backend.service;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.dto.client.CompactClientReadDto;
import org.d3javu.backend.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<CompactClientReadDto> getAllClients(){
        return this.clientRepository.findAllICompactClientReadDto();
    }

}
