package com.example.bookhub.dto.order;

public record OrderItemWithoutPriceResponseDto(Long id, Long bookId, int quantity) {
}
