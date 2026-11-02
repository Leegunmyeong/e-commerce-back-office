package com.ecommercebackoffice.dto;

import lombok.Getter;

@Getter
public class AdminSignupResponse {

    // 속성
    private String message;
    private Long adminId;
    private String adminStatus;
    private String adminEmail;

    // 생성자
    public AdminSignupResponse(String message, Long adminId, String adminStatus, String adminEmail) {
        this.message = message;
        this.adminId = adminId;
        this.adminStatus = adminStatus;
        this.adminEmail = adminEmail;
    }
}
