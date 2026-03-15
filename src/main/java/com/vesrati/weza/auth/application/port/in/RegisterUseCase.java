package com.vesrati.weza.auth.application.port.in;

import com.vesrati.weza.auth.application.dto.AuthResponse;
import com.vesrati.weza.auth.application.dto.LoginRequest;

public interface RegisterUseCase {
    AuthResponse execute(LoginRequest request);
}
