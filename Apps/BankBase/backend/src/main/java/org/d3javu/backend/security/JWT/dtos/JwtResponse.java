package org.d3javu.backend.security.JWT.dtos;

public record JwtResponse(
        String accessToken,
        String refreshToken
) { }
