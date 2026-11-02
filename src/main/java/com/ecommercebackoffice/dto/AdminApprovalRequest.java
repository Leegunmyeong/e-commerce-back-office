package com.ecommercebackoffice.dto;

import lombok.Getter;

@Getter
public class AdminApprovalRequest {

    // 속성
    private String status;
    private String rejectReason;
}
