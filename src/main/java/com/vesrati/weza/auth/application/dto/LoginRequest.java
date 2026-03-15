package com.vesrati.weza.auth.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Password é obrigatória")
        String password
) {
}
