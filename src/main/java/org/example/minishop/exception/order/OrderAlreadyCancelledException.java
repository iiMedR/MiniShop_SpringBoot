package org.example.minishop.exception.order;

public class OrderAlreadyCancelledException extends RuntimeException {
    public OrderAlreadyCancelledException(Long id) {
        super(
                "Order with id " + id + " is already cancelled"
        );
    }
}
