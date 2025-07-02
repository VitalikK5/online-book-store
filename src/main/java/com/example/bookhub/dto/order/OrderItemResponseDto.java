package com.example.bookhub.dto.order;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        int quantity,
        BigDecimal price
) {
}
