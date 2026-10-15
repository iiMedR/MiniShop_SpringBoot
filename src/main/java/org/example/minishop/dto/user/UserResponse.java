package org.example.minishop.dto.user;

public record UserResponse (
        Long id,
        String username,
        String email
) {
}
