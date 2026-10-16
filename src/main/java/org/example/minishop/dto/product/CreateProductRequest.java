package org.example.minishop.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "name is required")
        String name,
        @NotNull(message = "price is required")
        @Positive
        BigDecimal price,
        @NotNull(message = "stock is required")
        @PositiveOrZero
        Integer stock,
        @NotNull(message = "category id is required")
        Long categoryId
) {
}
