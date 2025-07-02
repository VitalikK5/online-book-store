package com.example.bookhub.service;

import com.example.bookhub.dto.order.CreateOrderRequestDto;
import com.example.bookhub.dto.order.OrderItemWithoutPriceResponseDto;
import com.example.bookhub.dto.order.OrderResponseDto;
import com.example.bookhub.mapper.OrderMapper;
import com.example.bookhub.model.CartItem;
import com.example.bookhub.model.Order;
import com.example.bookhub.model.OrderItem;
import com.example.bookhub.model.ShoppingCart;
import com.example.bookhub.model.User;
import com.example.bookhub.model.enums.Status;
import com.example.bookhub.repository.cartitem.CartItemRepository;
import com.example.bookhub.repository.order.OrderItemRepository;
import com.example.bookhub.repository.order.OrderRepository;
import com.example.bookhub.repository.shoppingcart.ShoppingCartRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository cartRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto placeOrder(CreateOrderRequestDto requestDto) {
        User currentUser = getCurrentUser();
        ShoppingCart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user: " + currentUser.getId()));
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot place an order with an empty cart");
        }

        Order order = new Order();
        order.setUser(currentUser);
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());

        Set<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    return orderItem;
                }).collect(Collectors.toSet());

        BigDecimal total = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        cartItemRepository.deleteAll(cartItems);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public Page<OrderResponseDto> getOrderHistory(Pageable pageable) {
        User user = getCurrentUser();
        return orderRepository.findAllByUser(user, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderResponseDto updateStatus(Long orderId, Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order with ID %d not found", orderId)));

        order.setStatus(status);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemWithoutPriceResponseDto> getAllItemsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order with ID %d not found", orderId)));

        return order.getOrderItems().stream()
                .map(item -> new OrderItemWithoutPriceResponseDto(
                        item.getId(),
                        item.getBook().getId(),
                        item.getQuantity()))
                .toList();
    }

    @Override
    public OrderItemWithoutPriceResponseDto getOrderItemById(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order with ID %d not found", orderId)));

        return order.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .map(item -> new OrderItemWithoutPriceResponseDto(
                        item.getId(),
                        item.getBook().getId(),
                        item.getQuantity()))
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(
                                "Item with ID %d not found in this order %d", itemId, orderId
                        )));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
