package com.example.bookhub.security;

import com.example.bookhub.dto.user.UserLoginRequestDto;
import com.example.bookhub.dto.user.UserLoginResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(
            @RequestBody @Valid UserLoginRequestDto loginRequestDto) {
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.email(),
                        loginRequestDto.password())
        );

        String token = jwtUtil.generateToken(loginRequestDto.email());
        return new UserLoginResponseDto(token);
    }
}
