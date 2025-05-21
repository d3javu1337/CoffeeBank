package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.security.clientsessions.ClientSessionsService;
import org.d3javu.backend.utils.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionsController {

    private final ClientSessionsService clientSessionsService;
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResponseEntity<?> getAllSessions() {
        return ResponseEntity.ok(this.clientSessionsService.getAllClientSessions(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSessionById(@RequestParam("id") UUID id) {
        this.clientSessionsService.closeSession(this.securityUtil.getClientEmail(), id);
        return ResponseEntity.ok().build();
    }

}
