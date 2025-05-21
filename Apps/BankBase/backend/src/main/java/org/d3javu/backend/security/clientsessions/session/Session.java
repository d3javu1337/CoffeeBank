package org.d3javu.backend.security.clientsessions.session;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@Document
public class Session {

    @Id
    private UUID id;

    private String refreshToken;

    private String deviceName;

    private String location;

    private Date expiredAt;

    private Date issuedAt;

    public Session(String refreshToken, String deviceName, String location, Date expiredAt, Date issuedAt) {
        this.id = UUID.randomUUID();
        this.refreshToken = refreshToken;
        this.deviceName = deviceName;
        this.location = location;
        this.expiredAt = expiredAt;
        this.issuedAt = issuedAt;
    }

    public Session(String deviceName, String refreshToken, Date expiredAt, Date issuedAt) {
        this.id = UUID.randomUUID();
        this.refreshToken = refreshToken;
        this.deviceName = deviceName;
        this.expiredAt = expiredAt;
        this.issuedAt = issuedAt;
    }

    public Session(String refreshToken, Date issuedAt, Date expiredAt) {
        this.id = UUID.randomUUID();
        this.refreshToken = refreshToken;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
    }



}
