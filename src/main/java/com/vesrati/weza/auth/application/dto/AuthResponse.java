package com.vesrati.weza.auth.application.dto;

import com.vesrati.weza.auth.domain.model.User;

public record AuthResponse(
        String       accessToken,
        String       refreshToken,
        String       tokenType,
        long         expiresIn,    // segundos
        UserResponse user
) {
    public static AuthResponse of(String accessToken, String refreshToken,
                                  long expiresIn, User user) {
        return new AuthResponse(
                accessToken, refreshToken,
                "Bearer", expiresIn,
                UserResponse.from(user)
        );
    }
}
