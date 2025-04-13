package org.d3javu.backend.utils;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.service.ClientService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurityUtil {

    private final ClientService clientService;

    public String getClientEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Long getClientId() {
        return this.clientService.getClientIdByEmail(getClientEmail());
    }

    public Long getClientId(String email) {
        return this.clientService.getClientIdByEmail(email);
    }

}
