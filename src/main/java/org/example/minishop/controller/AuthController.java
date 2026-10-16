package org.example.minishop.controller;

import jakarta.validation.Valid;
import org.example.minishop.dto.auth.AuthResponse;
import org.example.minishop.dto.auth.LoginRequest;
import org.example.minishop.dto.auth.RegisterRequest;
import org.example.minishop.dto.user.UserResponse;
import org.example.minishop.service.auth.AuthService;
import org.example.minishop.service.auth.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    public AuthController(AuthService authService,  AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse Register(@Valid @RequestBody RegisterRequest request) {
        return authService.Register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> Login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        String token = jwtService.generateToken(authentication.getName());
        return ResponseEntity.ok((new AuthResponse(token)));
    }
}
