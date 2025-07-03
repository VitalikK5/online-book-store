package com.example.bookhub.dto.order;

public record OrderItemDto(Long id, Long bookId, int quantity) {
}
