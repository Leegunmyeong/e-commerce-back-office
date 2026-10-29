package com.ecommercebackoffice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminSignupRequest {

    private String adminName;
    private String adminEmail;
    private String password;
    private String adminPhoneNumber;
    private String adminRole;
}
