package com.ecommercebackoffice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_email", nullable = false, length = 40, unique = true)
    private String customerEmail;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "customer_status", nullable = false, length = 20)
    private String customerStatus;

    @Column(name = "customer_registered_at", nullable = false)
    private LocalDateTime customerRegisteredAt;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;
    /**
     * 고객 정보 수정 메서드
     */
    public void updateCustomerInfo(String name, String email, String phone) {
        this.customerName = name;
        this.customerEmail = email;
        this.phoneNumber = phone;
    }
}