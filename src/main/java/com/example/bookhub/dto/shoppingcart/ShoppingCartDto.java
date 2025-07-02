package com.example.bookhub.dto.shoppingcart;

import com.example.bookhub.dto.cartitem.CartItemDto;
import java.util.List;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems;
}
