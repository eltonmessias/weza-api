package com.vesrati.weza.auth.application.usecase;


import com.vesrati.weza.auth.application.dto.AuthResponse;
import com.vesrati.weza.auth.application.port.in.RefreshTokenUseCase;
import com.vesrati.weza.auth.domain.exception.AuthException;
import com.vesrati.weza.auth.domain.model.User;
import com.vesrati.weza.auth.domain.port.out.TokenPort;
import com.vesrati.weza.auth.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final UserRepositoryPort userRepository;
    private final TokenPort tokenPort;


    @Override
    @Transactional(readOnly = true)
    public AuthResponse execute(String refreshToken) {
        if(!tokenPort.isValidToken(refreshToken))
            throw new AuthException("Invalid refresh token or expired");
        if(!tokenPort.isRefreshToken(refreshToken))
            throw new AuthException("The given token is not a refresh token");

        UUID userId = tokenPort.extractedUserId(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("User not found"));

        user.assertActivate();

        return AuthResponse.of(
                tokenPort.generateToken(user),
                tokenPort.generateRefreshToken(user),
                3600L,
                user
        );
    }
}
