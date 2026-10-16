package org.example.minishop.exception.user;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String email) {
        super(
                "User with email " + email + " does not exist"
        );
    }
}
