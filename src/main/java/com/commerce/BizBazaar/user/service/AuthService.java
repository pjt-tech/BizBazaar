package com.commerce.BizBazaar.user.service;

import com.commerce.BizBazaar.global.dto.ApiResponse;
import com.commerce.BizBazaar.global.util.JwtUtil;
import com.commerce.BizBazaar.user.dto.AuthResponseDto;
import com.commerce.BizBazaar.user.entity.RefreshToken;
import com.commerce.BizBazaar.user.entity.User;
import com.commerce.BizBazaar.user.repository.RefreshTokenRepository;
import com.commerce.BizBazaar.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public ApiResponse<AuthResponseDto> login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String accessToken = jwtUtil.generateToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        // RefreshToken을 DB에 저장
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUsername(username);
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        AuthResponseDto authResponse = new AuthResponseDto(accessToken, refreshToken);
        return new ApiResponse<>(true, "Login successful", authResponse, 200); // 로그인 성공
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


