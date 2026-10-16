package org.example.minishop.exception.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super(
                "Could not find product with id " + id
        );
    }
}
