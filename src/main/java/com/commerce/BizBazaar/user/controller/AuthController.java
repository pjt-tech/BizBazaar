package com.commerce.BizBazaar.user.controller;

import com.commerce.BizBazaar.global.dto.ApiResponse;
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
        return "register"; // register.html로 이동
    }

    @GetMapping("/vendor/dashboard")
    public String vendorPage() {
        return "vendor-dashboard";
    }

    @GetMapping("/customer/dashboard")
    public String customerPage() {
        return "customer-home";
    }

    @PostMapping("/auth/register")
    public String register(@ModelAttribute User user) {
        // 회원가입 처리
        authService.register(user);
        return "redirect:/"; // 회원가입 성공 후 홈 화면으로 리다이렉트
    }

    @PostMapping("/auth/login")
    public String login(@RequestParam String username, @RequestParam String password, @RequestParam String role, Model model, HttpServletRequest request) {
        ApiResponse<String> authResponse = authService.login(username, password);

        if (authResponse.getData() != null) {
            // 로그인 성공 시 세션에 사용자 정보 저장
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("role", role);

            // role에 따라 리다이렉트 처리
            if ("VENDOR".equalsIgnoreCase(role)) {
                return "redirect:/vendor/dashboard";  // VENDOR 역할이면 판매자 대시보드로 리다이렉트
            }
            else if ("CUSTOMER".equalsIgnoreCase(role)) {
                return "redirect:/customer/dashboard";  // CUSTOMER 역할이면 고객 대시보드로 리다이렉트
            }

            return "redirect:/";  // 로그인 성공 후 홈 화면으로 리다이렉트
        }

        model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
        return "login";  // 로그인 실패 시 로그인 페이지로 돌아가기
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 세션 무효화
        request.getSession().invalidate();
        return "redirect:/";  // 로그인 페이지로 리다이렉트
    }
}


