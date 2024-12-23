package com.commerce.BizBazaar.user.controller;

import com.commerce.BizBazaar.user.entity.User;
import com.commerce.BizBazaar.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // 관리자 대시보드 페이지
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<User> users = userService.getAllUsers();  // 관리자 목록 조회
        model.addAttribute("admins", users);
        return "adminDashboard";
    }

    // 사용자 정보 수정 페이지
    @GetMapping("/edit/{id}")
    public String editUserPage(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "editUser";
    }

    // 사용자 정보 수정 처리
    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);  // 사용자 정보 업데이트
        return "redirect:/admin/dashboard";
    }

    // 사용자 삭제
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);  // 사용자 삭제
        return "redirect:/admin/dashboard";
    }

    // 사용자 추가 페이지
    @GetMapping("/create")
    public String createUserPage() {
        return "createUser";
    }

    // 사용자 추가 처리
    @PostMapping("/create")
    public String createUser(@ModelAttribute User user) {
        userService.createUser(user);  // 사용자 추가
        return "redirect:/admin/dashboard";
    }
}
