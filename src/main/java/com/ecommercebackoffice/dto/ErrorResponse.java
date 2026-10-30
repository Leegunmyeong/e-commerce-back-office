package com.ecommercebackoffice.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {

    // 속성
    private String message;
    private int status;

    // 생성자
    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
