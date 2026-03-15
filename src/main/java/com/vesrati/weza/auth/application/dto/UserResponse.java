package com.vesrati.weza.auth.application.dto;

import com.vesrati.weza.auth.domain.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String        name,
        String        email,
        String        phone,
        boolean       emailVerified,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail().value(),
                user.getPhoneNumber() != null ? user.getPhoneNumber().value() : null,
                user.isEmailVerified(),
                user.getCreatedAt()
        );
    }
}
