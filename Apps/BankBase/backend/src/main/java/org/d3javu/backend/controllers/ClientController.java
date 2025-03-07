package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.dto.client.CompactClientReadDto;
import org.d3javu.backend.service.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<CompactClientReadDto> getAllClients() {
        return this.clientService.getAllClients();
    }

}

