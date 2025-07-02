package com.example.bookhub.service;

import com.example.bookhub.dto.order.CreateOrderRequestDto;
import com.example.bookhub.dto.order.OrderItemWithoutPriceResponseDto;
import com.example.bookhub.dto.order.OrderResponseDto;
import com.example.bookhub.model.enums.Status;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto placeOrder(CreateOrderRequestDto requestDto);

    Page<OrderResponseDto> getOrderHistory(Pageable pageable);

    OrderResponseDto updateStatus(Long orderId, Status status);

    List<OrderItemWithoutPriceResponseDto> getAllItemsByOrderId(Long orderId);

    OrderItemWithoutPriceResponseDto getOrderItemById(Long orderId, Long itemId);
}
