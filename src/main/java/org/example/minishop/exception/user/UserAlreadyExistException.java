package org.example.minishop.exception.user;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String email) {
        super(
                "User with email " + email + " already exists"
        );
    }
}
