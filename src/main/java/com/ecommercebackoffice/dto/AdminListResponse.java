package com.ecommercebackoffice.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminListResponse {

    // 속성
    private Long id;
    private String adminName;
    private String adminEmail;
    private String adminPhoneNumber;
    private String adminRole;
    private String adminStatus;
    private LocalDateTime adminRegisteredAt;
    private LocalDateTime adminApprovedAt;

    // 생성자
    public AdminListResponse(Long id, String adminName, String adminEmail,
                             String adminPhoneNumber, String adminRole,
                             String adminStatus, LocalDateTime adminRegisteredAt,
                             LocalDateTime adminApprovedAt) {
        this.id = id;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminPhoneNumber = adminPhoneNumber;
        this.adminRole = adminRole;
        this.adminStatus = adminStatus;
        this.adminRegisteredAt = adminRegisteredAt;
        this.adminApprovedAt = adminApprovedAt;
    }
}