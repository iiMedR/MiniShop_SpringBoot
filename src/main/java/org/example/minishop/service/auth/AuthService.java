package org.example.minishop.service.auth;

import org.example.minishop.dto.auth.RegisterRequest;
import org.example.minishop.dto.user.UserResponse;
import org.example.minishop.exception.user.UserAlreadyExistException;
import org.example.minishop.model.User;
import org.example.minishop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserResponse Register(RegisterRequest request) {
        User user =  new User();
        if(userRepository.existsByEmail(request.email())){
            throw new UserAlreadyExistException(request.email());
        }
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        return toResponse(userRepository.save(user));
    }
}
