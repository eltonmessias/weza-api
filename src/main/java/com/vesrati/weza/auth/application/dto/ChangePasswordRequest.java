package com.vesrati.weza.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Password actual é obrigatória")
        String currentPassword,

        @NotBlank(message = "Nova password é obrigatória")
        @Size(min = 8, message = "Nova password deve ter mínimo 8 caracteres")
        String newPassword
) {
}
