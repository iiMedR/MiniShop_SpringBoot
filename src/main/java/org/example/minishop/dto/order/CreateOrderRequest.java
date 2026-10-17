package org.example.minishop.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest (
    @NotEmpty List<@Valid OrderItemRequest> items
){
}
