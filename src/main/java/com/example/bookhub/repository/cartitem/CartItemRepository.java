package com.example.bookhub.repository.cartitem;

import com.example.bookhub.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);

    Optional<CartItem> findByShoppingCartIdAndBookId(Long cartId, Long bookId);
}
