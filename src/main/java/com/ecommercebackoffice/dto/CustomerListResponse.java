package com.ecommercebackoffice.dto;

import com.ecommercebackoffice.entity.Customer;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"currentPage", "pageSize", "totalCount", "totalPage", "customerList"}) // 순서 강제 고정
public class CustomerListResponse {

    private int currentPage;
    private int pageSize;
    private long totalCount;
    private int totalPage;
    private List<CustomerDto> customerList;
    // Page<Customer>를 받아서 필드를 채워주는 생성자 추가!
    public CustomerListResponse(org.springframework.data.domain.Page<Customer> customerPage) {
        this.currentPage = customerPage.getNumber() + 1; // page는 0부터 시작하므로 +1
        this.pageSize = customerPage.getSize();
        this.totalCount = customerPage.getTotalElements();
        this.totalPage = customerPage.getTotalPages();

        // Page 안에 있는 Customer 엔티티들을 CustomerDto로 변환
        this.customerList = customerPage.getContent().stream()
                .map(c -> CustomerDto.builder()
                        .id(c.getId())
                        .name(c.getCustomerName())
                        .email(c.getCustomerEmail())
                        .phone(c.getPhoneNumber())
                        .status(c.getCustomerStatus())
                        .joinedAt(c.getCustomerRegisteredAt())
                        .build())
                .toList();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CustomerDto {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String status;
        private LocalDateTime joinedAt;
    }
}