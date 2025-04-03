package org.d3javu.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.client.CompactClientReadDto;
import org.d3javu.backend.security.JWT.JwtCore;
import org.d3javu.backend.security.JWT.TokenType;
import org.d3javu.backend.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final JwtCore jwtCore;
    private final ClientService clientService;
    @GetMapping
    public ResponseEntity<?> getClient() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(this.clientService.getClientByEmail(email));
    }

}

