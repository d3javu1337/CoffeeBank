package org.d3javu.backend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.requests.IdRequest;
import org.d3javu.backend.dto.requests.NameRequest;
import org.d3javu.backend.dto.requests.account.AccountIdRequest;
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
    public ResponseEntity<?> getAccount(@RequestBody(required = false) AccountIdRequest request) {
        if(request == null || request.accountId() == null) {
            return ResponseEntity.ok(this.accountService.getAccounts());
        }

        var t = this.accountService.getAccount(request.accountId(), this.securityUtil.getClientId());

        if(t.isEmpty()){
            log.warn("requested account id={} which does not belongs to userId={}", request.accountId(),
                    this.securityUtil.getClientId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unable to do this");
        }

        return ResponseEntity.ok(t.get());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createAccount() {
        this.accountService.createAccount(this.securityUtil.getClientEmail());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping("/{id}")
    public void renameAccount(@RequestBody NameRequest nameRequest, @PathVariable Long id) {
        this.accountService.renameAccount(this.securityUtil.getClientEmail(), id, nameRequest.name());
    }

//    @DeleteMapping("/{id}")
//    public void closeAccount(@PathVariable Long id) {    }

}