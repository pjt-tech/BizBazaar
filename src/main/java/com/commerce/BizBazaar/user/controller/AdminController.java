package com.commerce.BizBazaar.user.controller;

import com.commerce.BizBazaar.user.entity.Customer;
import com.commerce.BizBazaar.user.entity.User;
import com.commerce.BizBazaar.user.entity.Vendor;
import com.commerce.BizBazaar.user.service.CustomerService;
import com.commerce.BizBazaar.user.service.UserService;
import com.commerce.BizBazaar.user.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final VendorService vendorService;
    private final CustomerService customerService;

    @Autowired
    public AdminController(UserService userService, VendorService vendorService, CustomerService customerService) {
        this.userService = userService;
        this.vendorService = vendorService;
        this.customerService = customerService;
    }

    // 관리자 목록 조회
    @GetMapping("/admins")
    @ResponseBody
    public List<User> getAdmins() {
        return userService.getAllUsers();  // 사용자 서비스에서 관리자 목록을 반환
    }

    // 벤더 목록 조회
    @GetMapping("/vendors")
    @ResponseBody
    public List<Vendor> getVendors() {
        return vendorService.getAllVendors();  // 벤더 서비스에서 벤더 목록을 반환
    }

    // 고객 목록 조회
    @GetMapping("/customers")
    @ResponseBody
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();  // 고객 서비스에서 고객 목록을 반환
    }

    // 관리자 대시보드 페이지
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<User> users = userService.getAllUsers();  // 관리자 목록 조회
        List<Vendor> vendors = vendorService.getAllVendors();  // 벤더 목록 조회
        List<Customer> customers = customerService.getAllCustomers();  // 고객 목록 조회

        model.addAttribute("admins", users);
        model.addAttribute("vendors", vendors);
        model.addAttribute("customers", customers);

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

    // 벤더 수정 페이지
    @GetMapping("/editVendor/{id}")
    public String editVendorPage(@PathVariable Long id, Model model) {
        Vendor vendor = vendorService.getVendorById(id);
        model.addAttribute("vendor", vendor);
        return "editVendor";
    }

    // 벤더 정보 수정 처리
    @PostMapping("/editVendor/{id}")
    public String editVendor(@PathVariable Long id, @ModelAttribute Vendor vendor) {
        vendorService.updateVendor(id, vendor);
        return "redirect:/admin/dashboard";
    }

    // 벤더 삭제
    @GetMapping("/deleteVendor/{id}")
    public String deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
        return "redirect:/admin/dashboard";
    }

    // 벤더 추가 페이지
    @GetMapping("/createVendor")
    public String createVendorPage() {
        return "createVendor";
    }

    // 벤더 추가 처리
    @PostMapping("/createVendor")
    public String createVendor(@ModelAttribute Vendor vendor) {
        vendorService.createVendor(vendor);
        return "redirect:/admin/dashboard";
    }

    // 고객 수정 페이지
    @GetMapping("/editCustomer/{id}")
    public String editCustomerPage(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        return "editCustomer";
    }

    // 고객 정보 수정 처리
    @PostMapping("/editCustomer/{id}")
    public String editCustomer(@PathVariable Long id, @ModelAttribute Customer customer) {
        customerService.updateCustomer(id, customer);
        return "redirect:/admin/dashboard";
    }

    // 고객 삭제
    @GetMapping("/deleteCustomer/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/admin/dashboard";
    }

    // 고객 추가 페이지
    @GetMapping("/createCustomer")
    public String createCustomerPage() {
        return "createCustomer";
    }

    // 고객 추가 처리
    @PostMapping("/createCustomer")
    public String createCustomer(@ModelAttribute Customer customer) {
        customerService.createCustomer(customer);
        return "redirect:/admin/dashboard";
    }
}


