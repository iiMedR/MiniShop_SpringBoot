package org.example.minishop.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest (
        @NotNull(message = "product id is required") Long productId,
        @NotNull(message = "quantity is required") @Positive Integer quantity
){
}
