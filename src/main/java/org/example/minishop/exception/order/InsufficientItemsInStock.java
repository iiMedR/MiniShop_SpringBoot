package org.example.minishop.exception.order;

public class InsufficientItemsInStock extends RuntimeException {
    public InsufficientItemsInStock(Integer stock) {
        super(
                "Insufficient items in stock: " + stock
        );
    }
}
