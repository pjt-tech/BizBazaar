package com.commerce.BizBazaar.user.controller;

import com.commerce.BizBazaar.global.dto.ApiResponse;
import com.commerce.BizBazaar.user.dto.AuthResponseDto;
import com.commerce.BizBazaar.user.entity.User;
import com.commerce.BizBazaar.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
        return "register"; // 관리자 회원가입 페이지로 이동
    }

    @PostMapping("/auth/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        // "ROLE_ADMIN" 역할을 자동으로 부여
        authService.registerAsAdmin(username, password);
        return "redirect:/login";  // 회원가입 후 로그인 페이지로 리다이렉트
    }

    @PostMapping("/auth/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpServletResponse response) {
        ApiResponse<AuthResponseDto> authResponse = authService.login(username, password);

        if (authResponse.getData() != null) {
            String role = authResponse.getData().getRole();

            if ("ADMIN".equalsIgnoreCase(role)) {
                return "redirect:/admin/dashboard";  // 관리자 대시보드로 리다이렉트
            }
        }

        model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
        return "login";  // 로그인 실패 시 로그인 페이지로 돌아가기
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard"; // 관리자 대시보드 페이지
    }
}


