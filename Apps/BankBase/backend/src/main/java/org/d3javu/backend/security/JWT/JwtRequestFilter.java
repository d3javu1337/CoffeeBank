package org.d3javu.backend.security.JWT;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;

import static org.d3javu.backend.security.JWT.TokenType.ACCESS;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtCore jwtCore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            jwtToken = authHeader.substring(7);
            try{
                username = this.jwtCore.getUsername(jwtToken, ACCESS);
            }
            catch (ExpiredJwtException e){
                log.info("Expired JWT token");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
            catch (SignatureException e){
                log.info("Invalid JWT token");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var auth = new UsernamePasswordAuthenticationToken(username, null,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
