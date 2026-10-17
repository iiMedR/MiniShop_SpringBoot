package org.example.minishop.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse (
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal price
) {
}
