package com.vesrati.weza.auth.infrastructure.security;


import com.vesrati.weza.auth.domain.model.User;
import com.vesrati.weza.auth.domain.port.out.TokenPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenAdapter implements TokenPort {

    private final SecretKey key;
    private final long accessExpiry;
    private final long refreshExpiry;

    public JwtTokenAdapter(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.access-expiry:3600000") long accessExpiry,
            @Value("${app.refresh-expiry:604800000") long refreshExpiry
    ){
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiry = accessExpiry;
        this.refreshExpiry = refreshExpiry;
    }

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail().value())
                .claim("name", user.getName())
                .claim("type", "ACCESS")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiry))
                .signWith(key)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("type", "REFRESH")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiry))
                .signWith(key)
                .compact();
    }

    @Override public UUID extractedUserId(String token) { return (UUID) parseClaims(token).get("id"); }
    @Override public String extractEmail(String token) { return (String) parseClaims(token).get("email"); }
    @Override public boolean isRefreshToken(String token) { return "REFRESH".equals(extractEmail(token)); }

    @Override
    public boolean isValidToken(String token) {
        try {parseClaims(token); return true;}
        catch (JwtException | IllegalArgumentException e) { return false; }
    }


    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
    }
}
