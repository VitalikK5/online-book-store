package com.example.bookhub.controller;

import com.example.bookhub.dto.order.CreateOrderRequestDto;
import com.example.bookhub.dto.order.OrderItemWithoutPriceResponseDto;
import com.example.bookhub.dto.order.OrderResponseDto;
import com.example.bookhub.dto.order.UpdateOrderStatusRequestDto;
import com.example.bookhub.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Management", description = "Endpoints for managing orders")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place a new order for the current user")
    public OrderResponseDto placeOrder(
            @Valid @RequestBody CreateOrderRequestDto requestDto) {
        return orderService.placeOrder(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order history for the current user")
    public List<OrderResponseDto> getOrderHistory(Pageable pageable) {
        return orderService.getOrderHistory(pageable).getContent();
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status (ADMIN only)")
    public OrderResponseDto updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequestDto dto) {
        return orderService.updateStatus(orderId, dto.status());
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all items by order ID")
    public List<OrderItemWithoutPriceResponseDto> getAllItems(
            @PathVariable Long orderId) {
        return orderService.getAllItemsByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order item by order ID and item ID")
    public OrderItemWithoutPriceResponseDto getItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }
}
