package org.example.minishop.dto.product;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        @Positive
        BigDecimal price,
        @PositiveOrZero
        Integer stock
) {
}
