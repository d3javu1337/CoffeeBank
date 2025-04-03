package org.d3javu.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.security.JWT.JwtCore;
import org.d3javu.backend.security.JWT.dtos.JwtResponse;
import org.d3javu.backend.security.clientsessions.ClientSessions;
import org.d3javu.backend.security.clientsessions.ClientSessionsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;


@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtCore jwtCore;
    private final ClientSessionsService clientSessionsService;

    @Transactional
    public JwtResponse login(@NotNull UserDetails userDetails, HttpServletRequest request) {

        var refreshToken = jwtCore.generateRefreshToken(userDetails);

        var session = new ClientSessions(userDetails.getUsername());
//        session.addSession(refreshToken);

        this.clientSessionsService.registerClientSession
                (userDetails.getUsername(), refreshToken, request.getHeader("User-Agent"));

        return new JwtResponse(
                this.jwtCore.generateAccessToken(userDetails),
                refreshToken
        );
    }

    public JwtResponse refresh(@NotNull UserDetails userDetails) {
        /*
        do the session delete logic when refresh token were user
         */
        return new JwtResponse(
                this.jwtCore.generateAccessToken(userDetails),
                this.jwtCore.generateRefreshToken(userDetails)
        );
    }

    public void logout() {}

}
