package org.d3javu.backend.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.client.ClientCreateDto;
import org.d3javu.backend.dto.client.ClientCreateRecord;
import org.d3javu.backend.dto.client.CompactClientReadDto;
import org.d3javu.backend.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<CompactClientReadDto> getAllClients() {
        return this.clientService.getAllClients();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @JsonDeserialize(as = CompactClientReadDto.class)
    public CompactClientReadDto createClient(@RequestBody ClientCreateRecord clientCreateRecord) {
        log.error("createClient: clientCreateRecord {}", clientCreateRecord);
        return this.clientService.registration(clientCreateRecord);
    }

}

