package org.example.minishop.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest (
        @NotBlank(message = "category name is required") String name
) {
}
