package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.requests.account.AccountRenameRequest;
import org.d3javu.backend.service.AccountService;
import org.d3javu.backend.utils.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/account")
@RestController
public class AccountController {

    private final AccountService accountService;
    private final SecurityUtil securityUtil;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<?> getAccount() {
        var t = this.accountService.getAccount(this.securityUtil.getClientId());

        return ResponseEntity.ok(t.orElse(null));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createAccount() {
        this.accountService.createAccount(this.securityUtil.getClientEmail());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping
    public void renameAccount(@RequestBody AccountRenameRequest accountRenameRequest) {
        this.accountService.renameAccount(
                this.securityUtil.getClientEmail(),
                this.securityUtil.getClientAccountId(),
                accountRenameRequest.newName());
    }

}