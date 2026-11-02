package com.ecommercebackoffice.dto;

import lombok.Getter;

@Getter
public class AdminLoginResponse {

    // 속성
    private String message;
    private Long adminId;

    // 생성자
    public AdminLoginResponse(String message, Long adminId) {
        this.message = message;
        this.adminId = adminId;
    }
}
