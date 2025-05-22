package org.d3javu.backend.dto.requests.account;

public record AccountRenameRequest(
        Long id,
        String newName
) {}
