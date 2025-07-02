package com.example.bookhub.dto.order;

import com.example.bookhub.model.enums.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(
        @NotNull(message = "Status is mandatory")
        Status status
) {
}