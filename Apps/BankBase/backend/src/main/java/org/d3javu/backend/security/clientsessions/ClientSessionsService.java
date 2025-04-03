package org.d3javu.backend.security.clientsessions;

import lombok.RequiredArgsConstructor;
import org.d3javu.backend.security.JWT.JwtCore;
import org.d3javu.backend.security.JWT.TokenType;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientSessionsService {

    private final JwtCore jwtCore;

    private final ClientSessionsRepository clientSessionsRepository;

    public void registerClientSession(String email, String token, String device) {
        var expiredAt = this.jwtCore.getExpiration(token, TokenType.REFRESH);
        var issuedAt = this.jwtCore.getIssuedAt(token, TokenType.REFRESH);
        if (!this.clientSessionsRepository.existsClientSessionsByEmail(email)) {

            this.clientSessionsRepository.save(new ClientSessions(email, token, issuedAt, expiredAt));
            return;
        }
        var sessions = this.clientSessionsRepository.findClientSessionsByEmail(email);
        sessions.addSession(device, token, issuedAt, expiredAt);
        this.clientSessionsRepository.save(sessions);
    }

    public List<ClientSessions> getAllClientSessions(String email) {
        return this.clientSessionsRepository.findAllByEmail(email);
    }

}
