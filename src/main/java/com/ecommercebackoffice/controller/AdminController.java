package com.ecommercebackoffice.controller;

import com.ecommercebackoffice.dto.AdminListResponse;
import com.ecommercebackoffice.dto.AdminLoginRequest;
import com.ecommercebackoffice.dto.AdminPageResponse;
import com.ecommercebackoffice.dto.AdminSignupRequest;
import com.ecommercebackoffice.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

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

    // 관리자 로그인
    @PostMapping("/login")
    public void login(@RequestBody AdminLoginRequest request,
                      HttpServletRequest httpRequest) {
        // 관리자 로그인 처리
        adminService.login(request, httpRequest);
    }

    // 관리자 로그아웃
    @PostMapping("/logout")
    public void logout(HttpServletRequest httpRequest) {
        // 관리자 로그아웃 처리
        adminService.logout(httpRequest);
    }

    // 관리자 리스트 조회
    @GetMapping
    public AdminPageResponse getAdminList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {

        // 관리자 리스트 조회 결과 반환
        return adminService.getAdminList(keyword, role, status, page, size, httpRequest);
    }

    // 관리자 상세 조회
    @GetMapping("/{adminId}")
    public AdminListResponse getAdminDetail(
            @PathVariable Long adminId,
            HttpServletRequest httpRequest) {

        // 관리자 상세 조회
        return adminService.getAdminDetail(adminId, httpRequest);
    }
}