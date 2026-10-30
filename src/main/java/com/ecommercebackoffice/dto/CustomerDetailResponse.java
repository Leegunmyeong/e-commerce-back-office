package com.ecommercebackoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailResponse {
    private Long id;                  // 고객 고유 ID
    private String name;              // 고객 이름
    private String email;             // 이메일
    private String phone;             // 전화번호
    private String status;            // 회원 상태
    private LocalDateTime joinedAt;   // 가입일
}