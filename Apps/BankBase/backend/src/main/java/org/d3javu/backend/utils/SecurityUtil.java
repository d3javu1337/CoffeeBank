package org.d3javu.backend.utils;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.service.AccountService;
import org.d3javu.backend.service.ClientService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private final ClientService clientService;
    private final AccountService accountService;

    public SecurityUtil(@Lazy ClientService clientService, @Lazy AccountService accountService) {
        this.clientService = clientService;
        this.accountService = accountService;
    }

    public String getClientEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Long getClientId() {
        return this.clientService.getClientIdByEmail(getClientEmail());
    }

    public Long getClientId(String email) {
        return this.clientService.getClientIdByEmail(email);
    }

    public Long getClientAccountId() {
        return this.accountService.getAccountIdByClientId(this.getClientId());
    }
}
