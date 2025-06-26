package com.example.bookhub.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank(message = "Email can not be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password can not be blank")
        @Size(min = 8, max = 20)
        String password
) {
}
