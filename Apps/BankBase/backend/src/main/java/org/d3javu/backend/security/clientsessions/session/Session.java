package org.d3javu.backend.security.clientsessions.session;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
public class Session {

    private String refreshToken;

    private String deviceName;

    private String location;

    private Date expiredAt;

    private Date issuedAt;

    public Session(String refreshToken, String deviceName, String location, Date expiredAt, Date issuedAt) {
        this.refreshToken = refreshToken;
        this.deviceName = deviceName;
        this.location = location;
        this.expiredAt = expiredAt;
        this.issuedAt = issuedAt;
    }

    public Session(String deviceName, String refreshToken, Date expiredAt, Date issuedAt) {
        this.refreshToken = refreshToken;
        this.deviceName = deviceName;
        this.expiredAt = expiredAt;
        this.issuedAt = issuedAt;
    }

    public Session(String refreshToken, Date issuedAt, Date expiredAt) {
        this.refreshToken = refreshToken;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
    }



}
