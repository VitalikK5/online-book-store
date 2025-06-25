package com.example.bookhub.service;

import com.example.bookhub.dto.user.UserRegistrationRequestDto;
import com.example.bookhub.dto.user.UserResponseDto;
import com.example.bookhub.exception.RegistrationException;
import com.example.bookhub.mapper.UserMapper;
import com.example.bookhub.model.User;
import com.example.bookhub.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with this email: "
                    + requestDto.getEmail() + " already exist");
        }
        User savedUser = userRepository.save(userMapper.toModel(requestDto));
        return userMapper.modelToResponse(savedUser);
    }
}
