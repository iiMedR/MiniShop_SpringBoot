package org.example.minishop.exception.order;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long orderId) {
        super(
                "Order with id " + orderId + " not found"
        );
    }
}
