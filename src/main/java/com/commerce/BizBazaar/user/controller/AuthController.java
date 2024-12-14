package com.commerce.BizBazaar.user.controller;

import com.commerce.BizBazaar.global.dto.ApiResponse;
import com.commerce.BizBazaar.user.dto.AuthResponseDto;
import com.commerce.BizBazaar.user.entity.User;
import com.commerce.BizBazaar.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/")
    public String home() {
        return "login";  // 로그인 페이지로 리다이렉트
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // register.html로 이동
    }

    @PostMapping("/auth/register")
    public String register(@ModelAttribute User user) {
        // 회원가입 처리
        authService.register(user);
        return "redirect:/"; // 회원가입 성공 후 홈 화면으로 리다이렉트
    }

    @PostMapping("/auth/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpServletResponse response) {
        ApiResponse<AuthResponseDto> authResponse = authService.login(username, password);

        if (authResponse.getData() != null) {
            // 로그인 성공 시, 토큰을 쿠키에 저장
            String accessToken = authResponse.getData().getAccessToken();
            String refreshToken = authResponse.getData().getRefreshToken();

            // Access Token을 쿠키에 저장
            Cookie accessTokenCookie = new Cookie("access_token", accessToken);
            accessTokenCookie.setHttpOnly(true);  // JavaScript로 접근 불가
            accessTokenCookie.setMaxAge(3600);  // 1시간 유효
            accessTokenCookie.setPath("/");

            // Refresh Token을 쿠키에 저장
            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setMaxAge(2592000);  // 30일 유효
            refreshTokenCookie.setPath("/");

            // 쿠키를 응답에 추가
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            return "redirect:/";  // 로그인 성공 후 홈 화면으로 리다이렉트
        }

        model.addAttribute("error", "Invalid username or password");
        return "login";  // 로그인 실패 시 로그인 페이지로 돌아가기
    }


    @PostMapping("/refresh")
    public String refresh(@RequestParam String refreshToken, Model model) {
        ApiResponse<String> response = authService.refresh(refreshToken);
        if (response.getData() != null) {
            model.addAttribute("newToken", response.getData());
            return "redirect:/";  // 토큰 갱신 후 홈 화면으로 리다이렉트
        }
        model.addAttribute("error", "Invalid refresh token");
        return "refresh";  // 오류 발생 시 리프레시 페이지로 돌아가기
    }
}


