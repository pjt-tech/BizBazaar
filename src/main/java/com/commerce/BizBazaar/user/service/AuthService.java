package com.commerce.BizBazaar.user.service;

import com.commerce.BizBazaar.global.dto.ApiResponse;
import com.commerce.BizBazaar.user.dto.AuthRequestDto;
import com.commerce.BizBazaar.user.dto.AuthResponseDto;
import com.commerce.BizBazaar.user.entity.User;
import com.commerce.BizBazaar.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void registerAsAdmin(AuthRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String name = dto.getName();
        String email = dto.getEmail();

        // 이미 존재하는 사용자 체크 (서비스에서 처리)
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");  // 이미 존재하는 사용자일 경우 예외 처리
        }

        try {
            // "ROLE_ADMIN" 역할을 자동으로 설정
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
            user.setRoles("ROLE_ADMIN");
            user.setName(name);
            user.setEmail(email);

            // 관리자 계정 저장
            userRepository.save(user);
        } catch (Exception e) {
            // 예외가 발생한 경우
            throw new RuntimeException("회원가입 중 문제가 발생했습니다.");  // 예외 처리
        }
    }



    public ApiResponse<AuthResponseDto> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            User user = userOpt.get();
            String role = user.getRoles();

            AuthResponseDto responseDto = new AuthResponseDto(username, role);
            return new ApiResponse<>(true, "Login successful", responseDto, 200);
        }

        return new ApiResponse<>(false, "아이디 또는 비밀번호가 잘못되었습니다.", null, 401);
    }
}


