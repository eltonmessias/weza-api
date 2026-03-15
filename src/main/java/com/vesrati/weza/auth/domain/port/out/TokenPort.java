package com.vesrati.weza.auth.domain.port.out;

import com.vesrati.weza.auth.domain.model.User;

import java.util.UUID;

public interface TokenPort {
    String generateToken(User user);
    String generateRefreshToken(User user);
    UUID extractedUserId(String token);
    String extractEmail(String token);
    boolean isValidToken(String token);
    boolean isRefreshToken(String token);
}
