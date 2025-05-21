package org.d3javu.backend.security.clientsessions;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.security.JWT.JwtCore;
import org.d3javu.backend.security.JWT.TokenType;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ClientSessionsService {

    private final JwtCore jwtCore;

    private final ClientSessionsRepository clientSessionsRepository;

    @Async
    public void registerClientSession(String email, String token, String device) {
        var expiredAt = this.jwtCore.getExpiration(token, TokenType.REFRESH);
        var issuedAt = this.jwtCore.getIssuedAt(token, TokenType.REFRESH);
        if (!this.clientSessionsRepository.existsClientSessionsByEmail(email)) {

            this.clientSessionsRepository.save(new ClientSessions(email, token, issuedAt, expiredAt, device));
            return;
        }
        var sessions = this.clientSessionsRepository.findClientSessionsByEmail(email);
        sessions.addSession(device, token, issuedAt, expiredAt);
        this.clientSessionsRepository.save(sessions);
    }

    public List<ClientSessions> getAllClientSessions(String email) {
        return this.clientSessionsRepository.findAllByEmail(email);
    }

    @Async
    public void closeSession(String email, UUID sessionId) {
        var sessions = this.clientSessionsRepository.findClientSessionsByEmail(email);
        sessions
                .getSessions()
                .removeIf(session -> session.getId().equals(sessionId));
        this.clientSessionsRepository.save(sessions);
    }

}
