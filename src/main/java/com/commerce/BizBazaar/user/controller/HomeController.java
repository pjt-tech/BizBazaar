package com.commerce.BizBazaar.user.controller;

import com.commerce.BizBazaar.user.service.CustomerService;
import com.commerce.BizBazaar.user.service.UserService;
import com.commerce.BizBazaar.user.service.VendorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final VendorService vendorService;
    private final CustomerService customerService;

    @GetMapping
    public String homePage(HttpServletRequest request, Model model) {
        // 세션에서 로그인한 사용자 정보 가져오기
        HttpSession session = request.getSession();
        Object userSession = session.getAttribute("user");

        if (userSession == null) {
            return "redirect:/";  // 세션에 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        }

        // 홈 화면에 표시할 데이터 추가
        model.addAttribute("user", userSession);
        return "home";  // home.html로 이동
    }
}
