package org.example.minishop.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest (
        @NotBlank(message = "email is required") @Email(message = "email must be correct") String email,
        @NotBlank(message = "password is required") @Size(min = 8, message = "password must be at least 8 characters") String password
) {
}
