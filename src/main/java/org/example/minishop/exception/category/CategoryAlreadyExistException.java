package org.example.minishop.exception.category;

public class CategoryAlreadyExistException extends RuntimeException {
    public CategoryAlreadyExistException(String name) {
        super(
                "Category with name " + name + " already exists"
        );
    }
}
