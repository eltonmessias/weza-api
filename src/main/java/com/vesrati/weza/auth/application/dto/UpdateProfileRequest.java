package com.vesrati.weza.auth.application.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String name,
        String phone
) {
}
