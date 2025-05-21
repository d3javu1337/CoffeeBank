package org.d3javu.backend.security.clientsessions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.d3javu.backend.security.clientsessions.session.Session;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Document(collection = "ClientSessions")
public class ClientSessions {

    @Id
    private String email;

    private List<Session> sessions;

    public ClientSessions(String email) {
        this.email = email;
        this.sessions = new ArrayList<>();
    }

    public ClientSessions(String email, String token, Date issuedAt, Date expiredAt, String deviceName) {
        this.email = email;
        this.sessions = new ArrayList<>();
        var session = new Session(deviceName, token, issuedAt, expiredAt);
        this.sessions.add(session);
    }

    public void addSession(String device, String refreshToken, Date issuedAt, Date expiredAt) {
        this.sessions.add(new Session(device, refreshToken, issuedAt, expiredAt));
    }

}
