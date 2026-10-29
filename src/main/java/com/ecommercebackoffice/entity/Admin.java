package com.ecommercebackoffice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
public class Admin {

    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String adminName;

    @Column(length = 40, nullable = false)
    private String adminEmail;

    @Column(length = 60, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String adminPhoneNumber;

    @Column(length = 20, nullable = false)
    private String adminRole;

    @Column(length = 20, nullable = false)
    private String adminStatus;

    @Column(nullable = false)
    private LocalDateTime adminRegisteredAt;

    // 관리자 승인일
    @Column(nullable = true)
    private LocalDateTime adminApprovedAt;

    @Column(nullable = false)
    private Boolean isDelete;

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
        this.isDelete = false;
        this.adminStatus = "Pending Approval";
    }
}
