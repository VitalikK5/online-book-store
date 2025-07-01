package com.example.bookhub.service;

import com.example.bookhub.dto.user.UserRegistrationRequestDto;
import com.example.bookhub.dto.user.UserResponseDto;
import com.example.bookhub.exception.RegistrationException;
import com.example.bookhub.mapper.UserMapper;
import com.example.bookhub.model.Role;
import com.example.bookhub.model.ShoppingCart;
import com.example.bookhub.model.User;
import com.example.bookhub.model.enums.RoleName;
import com.example.bookhub.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookhub.repository.role.RoleRepository;
import com.example.bookhub.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with this email: "
                    + requestDto.getEmail() + " already exist");
        }

        User userToSave = userMapper.toModel(requestDto);
        userToSave.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Role " + RoleName.USER + " not found"));
        userToSave.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(userToSave);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(savedUser);
        shoppingCartRepository.save(cart);

        return userMapper.modelToResponse(userToSave);
    }
    @Override
    public Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email))
                .getId();
    }
}
