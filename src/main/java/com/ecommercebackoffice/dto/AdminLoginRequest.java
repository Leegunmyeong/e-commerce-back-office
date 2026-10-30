package com.ecommercebackoffice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginRequest {

    // 속성
    private String adminEmail;
    private String password;
}