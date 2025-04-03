package org.d3javu.backend.security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class JwtCore {

    @Value("${jwt.accept.secret}")
    private String accessSecret;

    @Value("${jwt.accept.lifetime}")
    private Duration accessLifetime;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.refresh.lifetime}")
    private Duration refreshLifetime;

    public String generateAccessToken(UserDetails userDetails) {
        var issuedDate = new Date();
        var expiredDate = new Date(issuedDate.getTime() + this.accessLifetime.toMillis());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, this.accessSecret)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        var issuedDate = new Date();
        var expiredDate = new Date(issuedDate.getTime() + this.refreshLifetime.toMillis());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, this.refreshSecret)
                .compact();
    }

    public String getUsername(String token, TokenType type) {
        return this.extractClaims(token, type).getSubject();
    }

    public Date getExpiration(String token, TokenType type) {
        return this.extractClaims(token, type).getExpiration();
    }

    public Date getIssuedAt(String token, TokenType type) {
        return this.extractClaims(token, type).getIssuedAt();
    }

    private Claims extractClaims(String token, TokenType type) {
        return switch (type){
            case ACCESS -> Jwts.parser().setSigningKey(this.accessSecret).build().parseClaimsJws(token).getBody();
            case REFRESH -> Jwts.parser().setSigningKey(this.refreshSecret).build().parseClaimsJws(token).getBody();
        };
    }

}