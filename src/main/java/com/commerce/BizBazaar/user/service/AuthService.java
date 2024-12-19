package com.commerce.BizBazaar.user.service;

import com.commerce.BizBazaar.global.dto.ApiResponse;
import com.commerce.BizBazaar.user.dto.AuthRequestDto;
import com.commerce.BizBazaar.user.dto.AuthResponseDto;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public String registerAsAdmin(AuthRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        // 이미 존재하는 사용자 체크
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/auth/register?error=User already exists"; // 이미 존재하는 경우 에러 메시지와 함께 리다이렉트
        }

        try {
            // "ROLE_ADMIN" 역할을 자동으로 설정
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
            user.setRoles("ROLE_ADMIN");

            // 관리자 계정 저장
            userRepository.save(user);

            // 성공적으로 회원가입된 경우 리다이렉트
            return "redirect:/login?success=Registration successful";
        } catch (Exception e) {
            // 예외가 발생한 경우
            return "redirect:/auth/register?error=Registration failed";
        }
    }

    public ApiResponse<AuthResponseDto> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            User user = userOpt.get();
            String role = user.getRoles();  // 관리자 역할 확인

            AuthResponseDto responseDto = new AuthResponseDto(username, role);
            return new ApiResponse<>(true, "Login successful", responseDto, 200);
        }

        return new ApiResponse<>(false, "아이디 또는 비밀번호가 잘못되었습니다.", null, 401); // 로그인 실패
    }
}


