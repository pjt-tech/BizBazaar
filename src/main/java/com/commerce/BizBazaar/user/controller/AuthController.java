package com.commerce.BizBazaar.user.controller;

import com.commerce.BizBazaar.global.dto.ApiResponse;
import com.commerce.BizBazaar.global.util.JwtUtil;
import com.commerce.BizBazaar.user.dto.AuthRequestDto;
import com.commerce.BizBazaar.user.dto.AuthResponseDto;
import com.commerce.BizBazaar.user.entity.User;
import com.commerce.BizBazaar.user.repository.UserRepository;
import com.commerce.BizBazaar.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        return authService.login(authRequestDto.getUsername(), authRequestDto.getPassword());
    }

    @PostMapping("/refresh")
    public ApiResponse<String> refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}


