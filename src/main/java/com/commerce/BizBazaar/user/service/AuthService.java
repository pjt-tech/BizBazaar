package com.commerce.BizBazaar.user.service;

import com.commerce.BizBazaar.global.dto.ApiResponse;
import com.commerce.BizBazaar.global.util.JwtUtil;
import com.commerce.BizBazaar.user.dto.AuthResponseDto;
import com.commerce.BizBazaar.user.entity.RefreshToken;
import com.commerce.BizBazaar.user.entity.User;
import com.commerce.BizBazaar.user.repository.RefreshTokenRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ApiResponse<String> register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return new ApiResponse<>(false, "User already exists", 400); // 이미 존재하는 경우
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 역할 앞에 "ROLE_" 접두사 추가
        String roles = user.getRoles(); // "ADMIN,USER" 형태로 가정
        String updatedRoles = Arrays.stream(roles.split(","))
                .map(role -> role.trim().startsWith("ROLE_") ? role.trim() : "ROLE_" + role.trim()) // ROLE_이 없는 경우 추가
                .collect(Collectors.joining(",")); // 다시 콤마로 조합

        user.setRoles(updatedRoles); // 업데이트된 역할 설정
        userRepository.save(user);
        return new ApiResponse<>(true, "Registration successful", null, 200);
    }

    @Transactional
    public ApiResponse<AuthResponseDto> login(String username, String password) {
        try {
            // 인증 처리
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // 토큰 생성
            String accessToken = jwtUtil.generateToken(username);
            String refreshToken = jwtUtil.generateRefreshToken(username);

            // RefreshToken을 DB에 저장 (DB에 중복 INSERT가 발생하지 않도록)
            RefreshToken refreshTokenEntity = refreshTokenRepository.findByUsername(username)
                    .orElse(new RefreshToken());  // 기존 RefreshToken이 있으면 업데이트, 없으면 새로 생성

            refreshTokenEntity.setUsername(username);
            refreshTokenEntity.setRefreshToken(refreshToken);
            refreshTokenRepository.save(refreshTokenEntity);  // DB에 저장

            // AuthResponseDto 생성
            AuthResponseDto authResponse = new AuthResponseDto(accessToken, refreshToken);
            return new ApiResponse<>(true, "Login successful", authResponse, 200); // 로그인 성공

        }
        catch (AuthenticationException e) {
            // 인증 실패 시 에러 로그
            log.error("Login failed for user: {}", username, e);
            return new ApiResponse<>(false, "아이디 또는 비밀번호가 잘못되었습니다.", null, 401); // 로그인 실패
        }
    }


    public ApiResponse<String> refresh(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);

            // 리프레시 토큰을 DB에서 확인
            Optional<RefreshToken> storedToken = refreshTokenRepository.findByUsername(username);
            if (storedToken.isPresent() && storedToken.get().getRefreshToken().equals(refreshToken)) {
                String newAccessToken = jwtUtil.generateToken(username);
                return new ApiResponse<>(true, "Token refreshed successfully", newAccessToken, 200); // 새로운 액세스 토큰 반환
            }
        }
        return new ApiResponse<>(false, "Invalid refresh token", 400); // 잘못된 리프레시 토큰
    }
}


