package com.example.bookhub.repository.order;

import com.example.bookhub.model.Order;
import com.example.bookhub.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "orderItems")
    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findAllByUser(User user, Pageable pageable);

}
