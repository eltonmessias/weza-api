package com.vesrati.weza.auth.application.port.in;

import com.vesrati.weza.auth.application.dto.AuthResponse;

public interface RefreshTokenUseCase {
    AuthResponse execute(String refreshToken);
}
