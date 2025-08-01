package com.example.bookhub.dto.shoppingcart;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartItemQuantityRequestDto {
    @Positive
    private int quantity;
}
