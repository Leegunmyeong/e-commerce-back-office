package com.ecommercebackoffice.controller;

import com.ecommercebackoffice.dto.AdminSignupRequest;
import com.ecommercebackoffice.service.AdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    // 속성
    private final AdminService adminService;

    // 생성자
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 기능
    // 관리자 회원가입
    @PostMapping("/signup")
    public void signup(@RequestBody AdminSignupRequest request) {
        adminService.signup(request);
    }
}
