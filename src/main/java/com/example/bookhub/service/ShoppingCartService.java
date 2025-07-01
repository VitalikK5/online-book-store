package com.example.bookhub.service;

import com.example.bookhub.dto.book.AddBookToCartRequestDto;
import com.example.bookhub.dto.shoppingcart.QuantityDto;
import com.example.bookhub.dto.shoppingcart.ShoppingCartDto;
import com.example.bookhub.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    ShoppingCartDto addBookToShoppingCart(AddBookToCartRequestDto dto);

    ShoppingCartDto updateQuantity(Long cartItemId, QuantityDto quantity);

    ShoppingCartDto removeBookFromShoppingCart(Long cartItemId);

    void createShoppingCart(User user);
}
