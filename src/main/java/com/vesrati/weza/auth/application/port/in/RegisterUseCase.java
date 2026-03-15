package com.vesrati.weza.auth.application.port.in;

import com.vesrati.weza.auth.application.dto.AuthResponse;
import com.vesrati.weza.auth.application.dto.LoginRequest;
import com.vesrati.weza.auth.application.dto.RegisterRequest;

public interface RegisterUseCase {
    AuthResponse execute(RegisterRequest request);
}
