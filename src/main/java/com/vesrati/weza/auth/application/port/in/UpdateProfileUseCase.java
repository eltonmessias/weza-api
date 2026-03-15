package com.vesrati.weza.auth.application.port.in;

import com.vesrati.weza.auth.application.dto.UpdateProfileRequest;
import com.vesrati.weza.auth.application.dto.UserResponse;

public interface UpdateProfileUseCase {
    UserResponse execute(UpdateProfileRequest request);
}
