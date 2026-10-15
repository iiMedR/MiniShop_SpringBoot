package org.example.minishop.controller;

import jakarta.validation.Valid;
import org.example.minishop.dto.auth.RegisterRequest;
import org.example.minishop.dto.user.UserResponse;
import org.example.minishop.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse Register(@Valid @RequestBody RegisterRequest request) {
        return authService.Register(request);
    }
}
