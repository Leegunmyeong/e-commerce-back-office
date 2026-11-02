package com.ecommercebackoffice.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminApprovalResponse {

    // 속성
    private String message;
    private Long adminId;
    private String adminStatus;
    private LocalDateTime adminApprovedAt;
    private String adminRejectReason;

    // 생성자
    public AdminApprovalResponse(String message, Long adminId,
                                 String adminStatus, LocalDateTime adminApprovedAt,
                                 String adminRejectReason) {
        this.message = message;
        this.adminId = adminId;
        this.adminStatus = adminStatus;
        this.adminApprovedAt = adminApprovedAt;
        this.adminRejectReason = adminRejectReason;
    }
}
