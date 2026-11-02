package com.ecommercebackoffice.dto;

import com.ecommercebackoffice.entity.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderListResponseDto {

    //속성
    private final Long id;
    private final String customerName;
    private final String productName;
    private final Long orderQuantity;
    private final Long totalPrice;
    private final LocalDateTime orderDate;
    private final OrderStatus orderStatus;
    private final String adminName;

    //생성자
    public OrderListResponseDto(Long id, String customerName, String productName, Long orderQuantity, Long totalPrice, LocalDateTime orderDate, OrderStatus orderStatus, String adminName) {
        this.id = id;
        this.customerName = customerName;
        this.productName = productName;
        this.orderQuantity = orderQuantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.adminName = adminName;
    }
    //기능
}
