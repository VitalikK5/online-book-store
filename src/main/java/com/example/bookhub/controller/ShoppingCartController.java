package com.example.bookhub.controller;

import com.example.bookhub.dto.book.AddBookToCartRequestDto;
import com.example.bookhub.dto.shoppingcart.ShoppingCartDto;
import com.example.bookhub.dto.shoppingcart.UpdateCartItemQuantityRequestDto;
import com.example.bookhub.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Get user's shopping cart", description = "Get user's shopping cart")
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @Operation(summary = "Add book to the shopping cart",
            description = "Add book to the shopping cart")
    public ShoppingCartDto addBookToShoppingCart(
            @RequestBody @Valid AddBookToCartRequestDto addBookRequestDto) {
        return shoppingCartService.addBookToShoppingCart(addBookRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("items/{cartItemId}")
    @Operation(summary = "Update quantity of a book",
            description = "Update quantity of a book in the shopping cart")
    public ShoppingCartDto updateQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemQuantityRequestDto quantityDto
    ) {
        return shoppingCartService.updateQuantity(cartItemId, quantityDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("items/{cartItemId}")
    @Operation(summary = "Remove book from shopping cart",
            description = "Remove a book from the shopping cart")
    public ShoppingCartDto removeBookFromShoppingCart(@PathVariable Long cartItemId) {
        return shoppingCartService.removeBookFromShoppingCart(cartItemId);
    }
}
