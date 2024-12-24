package com.commerce.BizBazaar.user.controller;

import com.commerce.BizBazaar.global.dto.ApiResponse;
import com.commerce.BizBazaar.user.dto.AuthRequestDto;
import com.commerce.BizBazaar.user.dto.AuthResponseDto;
import com.commerce.BizBazaar.user.entity.User;
import com.commerce.BizBazaar.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;


    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        // 세션에서 에러 메시지 가져오기
        HttpSession session = request.getSession();
        String errorMessage = (String) session.getAttribute("loginError");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("loginError");  // 메시지를 표시한 후 제거
        }
        return "login";
    }


    @GetMapping("/register")
    public String registerPage() {
        return "register"; // 관리자 회원가입 페이지로 이동
    }


    @PostMapping("/api/auth/signup")
    public String register(@ModelAttribute AuthRequestDto dto, RedirectAttributes redirectAttributes) {
        try {
            // 서비스에서 회원가입 처리를 요청
            authService.registerAsAdmin(dto);
            redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/";  // 회원가입 성공 후 로그인 페이지로 리다이렉트
        }
        catch (IllegalArgumentException e) {
            // 이미 존재하는 사용자가 있을 때 에러 메시지
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";  // 에러 발생 시 회원가입 페이지로 리다이렉트
        }
        catch (Exception e) {
            // 기타 예외 처리
            redirectAttributes.addFlashAttribute("errorMessage", "회원가입 중 문제가 발생했습니다. 다시 시도해주세요.");
            return "redirect:/register";  // 에러 발생 시 회원가입 페이지로 리다이렉트
        }
    }


    @PostMapping("/api/auth/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpServletRequest request) {
        ApiResponse<AuthResponseDto> authResponse = authService.login(username, password);

        if (authResponse.isSuccess()) {
            // 로그인 성공 시 세션에 사용자 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("user", authResponse.getData());  // 사용자 정보 저장

            String role = authResponse.getData().getRole();

            if ("ROLE_ADMIN".equalsIgnoreCase(role)) {
                return "redirect:/home";  // 로그인 후 홈 화면으로 이동
            } else {
                return "redirect:/user/home";  // 일반 사용자 페이지로 이동
            }
        }

        model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
        return "login";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  // 세션 종료
        }
        return "redirect:/";  // 로그인 페이지로 리다이렉트
    }
}


