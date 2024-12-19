package com.commerce.BizBazaar.user.service;

import com.commerce.BizBazaar.global.dto.ApiResponse;
import com.commerce.BizBazaar.user.entity.User;
import com.commerce.BizBazaar.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // 회원가입 처리
    public ApiResponse<String> register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return new ApiResponse<>(false, "User already exists", 400); // 이미 존재하는 경우
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String roles = user.getRoles();
        String updatedRoles = Arrays.stream(roles.split(","))
                .map(role -> role.trim().startsWith("ROLE_") ? role.trim() : "ROLE_" + role.trim())
                .collect(Collectors.joining(","));
        user.setRoles(updatedRoles);
        userRepository.save(user);
        return new ApiResponse<>(true, "Registration successful", null, 200);
    }

    @Transactional
    public ApiResponse<String> login(String username, String password) {
        try {
            // 인증 처리
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // 인증이 성공하면 사용자 정보를 세션에 저장하고, JWT를 생성하지 않습니다.
            return new ApiResponse<>(true, "Login successful", null, 200); // 로그인 성공

        } catch (AuthenticationException e) {
            log.error("Login failed for user: {}", username, e);
            return new ApiResponse<>(false, "아이디 또는 비밀번호가 잘못되었습니다.", null, 401); // 로그인 실패
        }
    }
}


