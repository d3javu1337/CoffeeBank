package org.d3javu.backend.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.d3javu.backend.dto.client.ClientAuth;
import org.d3javu.backend.dto.client.ClientCreateRecord;
import org.d3javu.backend.dto.client.CompactClientReadDto;
import org.d3javu.backend.security.JWT.JwtCore;
import org.d3javu.backend.security.JWT.dtos.JwtResponse;
import org.d3javu.backend.security.clientsessions.ClientSessionsService;
import org.d3javu.backend.service.AuthService;
import org.d3javu.backend.service.ClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.d3javu.backend.security.JWT.TokenType.ACCESS;
import static org.d3javu.backend.security.JWT.TokenType.REFRESH;

/**
 * logic of this should be look like only for registration, login and token generation. Logic:
 * Access token is valid? -> not here. In JwtRequestFiler. |
 * Access token is not valid -> 401-unauthorized -> request from frontend for new access token by refresh token. |
 * If refresh token also isn`t valid -> 401-unauthorized -> should ask a new pair of tokens by login and pwd. |
 * For "sessions" of user -> mongo with collection like user -> sessions (only refresh tokens). |
 * (should think about deleting expired tokens)
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtCore jwtCore;
    private final ClientService clientService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final ClientSessionsService clientSessionsService;

    @Value("${jwt.refresh.lifetime}")
    private Duration refreshLifetime;

    @Value("${jwt.refresh_cookie_name}")
    private String refreshCookieName;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompactClientReadDto createClient(@RequestBody ClientCreateRecord clientCreateRecord) {
        log.debug("createClient: clientCreateRecord {}", clientCreateRecord);
        return this.clientService.registration(clientCreateRecord);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ClientAuth clientAuth,
                                   HttpServletRequest request, HttpServletResponse response) {
        try{
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(clientAuth.email(), clientAuth.password()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }


        var client = this.clientService.loadUserByUsername(clientAuth.email());

        log.info("user-agent: {}", request.getHeader("User-Agent"));

        var tokens = this.authService.login(client, request);

        this.setCookie(response, tokens.refreshToken());



        log.debug("login method -> tokens: {}", tokens);

        return ResponseEntity.ok(tokens.accessToken());
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        var refreshToken = Arrays.stream(request.getCookies()).filter(en -> en.getName().equals(this.refreshCookieName)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)).getValue();

        var email = this.jwtCore.getUsername(refreshToken, REFRESH);

        var tokens = this.authService.refresh(this.clientService.loadUserByUsername(email));

        this.setCookie(response, tokens.refreshToken());

        log.debug("refresh method -> tokens: {}", tokens);

        return ResponseEntity.ok(tokens.accessToken());
    }

    public ResponseEntity<?> restorePassword(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok("restore password");
    }

    @GetMapping("/sessions")
    public ResponseEntity<?> getAllSessions() {
        return ResponseEntity.ok(this.clientSessionsService.getAllClientSessions(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public ResponseEntity<?> closeSession(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok("closeSession");
    }

/*        @GetMapping("/verify")
        public ResponseEntity<?> verify(HttpServletRequest request, HttpServletResponse response) {
            var token = request.getHeader("Authorization");
            var refreshCookie = new Cookie("refreshTest", "set refresh token here");
            refreshCookie.setHttpOnly(true);
    //        refreshCookie.setSecure(true);
            refreshCookie.setMaxAge((int)Duration.between(LocalDateTime.now(), LocalDateTime.now().plusMonths(2)).toSeconds());
            response.addCookie(refreshCookie);
            return ResponseEntity.ok(jwtCore.getUsername(token.substring(7), ACCESS));
        }*/

    private void setCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshCookie = new Cookie(this.refreshCookieName, refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge((int)Duration.between(LocalDateTime.now(),
                LocalDateTime.now().plus(this.refreshLifetime)).toSeconds());
        refreshCookie.setPath("***/***");
        response.addCookie(refreshCookie); // should do some mongo logic here about saving refresh as user`s sessions

        response.addCookie(refreshCookie);
    }

}