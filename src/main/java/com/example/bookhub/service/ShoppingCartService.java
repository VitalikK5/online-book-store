package com.example.bookhub.service;

import com.example.bookhub.dto.book.AddBookToCartRequestDto;
import com.example.bookhub.dto.shoppingcart.ShoppingCartDto;
import com.example.bookhub.dto.shoppingcart.UpdateCartItemQuantityRequestDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    ShoppingCartDto addBookToShoppingCart(AddBookToCartRequestDto dto);

    ShoppingCartDto updateQuantity(Long cartItemId, UpdateCartItemQuantityRequestDto quantityDto);

    ShoppingCartDto removeBookFromShoppingCart(Long cartItemId);
}
