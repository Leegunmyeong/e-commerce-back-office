package com.ecommercebackoffice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.YesNoConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
@SoftDelete(
        columnName = "is_delete",
        converter = YesNoConverter.class
)
@Getter
public class Admin {

    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String adminName;

    @Column(length = 40, nullable = false, unique = true)
    private String adminEmail;

    @Column(length = 60, nullable = false)
    private String password;

    // 관리자 전화번호 (010-1234-5678 형식)
    @Column(length = 20, nullable = false)
    private String adminPhoneNumber;

    @Column(length = 20, nullable = false)
    private String adminRole;

    // 관리자 상태 (가입 대기, 승인 거부, 활성, 비활성)
    @Column(length = 20, nullable = false)
    private String adminStatus;

    @Column(nullable = false)
    private LocalDateTime adminRegisteredAt;

    // 관리자 승인일
    @Column(nullable = true)
    private LocalDateTime adminApprovedAt;

    // 관리자 거부 사유
    @Column(length = 255, nullable = true)
    private String adminRejectReason;

    // 생성자
    public Admin() {

    }

    public Admin(String adminName, String adminEmail,
                 String password, String adminPhoneNumber,
                 String adminRole) {
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.password = password;
        this.adminPhoneNumber = adminPhoneNumber;
        this.adminRole = adminRole;
        this.adminRegisteredAt = LocalDateTime.now();
        this.adminStatus = "Pending Approval";
    }

    // 관리자 가입 승인 처리
    public void approve() {
        this.adminStatus = "Active";
        this.adminApprovedAt = LocalDateTime.now();
    }

    // 관리자 가입 거부 처리
    public void reject(String adminRejectReason) {
        this.adminStatus = "Approval Denied";
        this.adminRejectReason = adminRejectReason;
    }
}