package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.dto.account.CompactAccountReadDto;
import org.d3javu.backend.dto.client.CompactClientReadDto;
import org.d3javu.backend.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/account")
@RestController
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<?> getAccount() {
        return ResponseEntity.ok(this.accountService.getAccountByClientEmail
                (SecurityContextHolder.getContext().getAuthentication().getName()));
    }

}