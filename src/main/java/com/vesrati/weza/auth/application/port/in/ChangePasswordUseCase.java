package com.vesrati.weza.auth.application.port.in;

import com.vesrati.weza.auth.application.dto.ChangePasswordRequest;

import java.util.UUID;

public interface ChangePasswordUseCase {
    void execute(UUID userId, ChangePasswordRequest request);
}
