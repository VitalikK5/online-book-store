package com.example.bookhub.service;

import com.example.bookhub.dto.order.CreateOrderRequestDto;
import com.example.bookhub.dto.order.OrderItemDto;
import com.example.bookhub.dto.order.OrderResponseDto;
import com.example.bookhub.exception.OrderProcessingException;
import com.example.bookhub.mapper.OrderItemMapper;
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
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto placeOrder(CreateOrderRequestDto requestDto) {
        User currentUser = getCurrentUser();
        ShoppingCart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user: " + currentUser.getId()));

        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new OrderProcessingException(
                    "Cannot place an order with an empty cart. User ID: " + currentUser.getId()
            );
        }

        Order order = buildOrder(requestDto, currentUser, cartItems);
        cart.getCartItems().clear();

        return orderMapper.toDto(orderRepository.save(order));
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
    public List<OrderItemDto> getAllItemsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order with ID %d not found", orderId)));

        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByOrderIdAndId(orderId, itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Item with ID %d not found in order %d", itemId, orderId)
                ));

        return orderItemMapper.toDto(orderItem);
    }

    private Order buildOrder(CreateOrderRequestDto requestDto, User user, Set<CartItem> cartItems) {
        Order order = new Order();
        order.setUser(user);
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
        return order;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
