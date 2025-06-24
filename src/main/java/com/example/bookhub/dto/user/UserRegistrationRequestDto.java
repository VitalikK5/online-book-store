package com.example.bookhub.dto.user;

import com.example.bookhub.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@FieldMatch
@Data
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 20, message = "Password should have from 8 to 20 symbols")
    private String password;
    @NotBlank
    @Length(min = 8, max = 20, message = "Password should have from 8 to 20 symbols")
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
