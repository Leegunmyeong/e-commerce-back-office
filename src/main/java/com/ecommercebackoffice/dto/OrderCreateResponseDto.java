package com.ecommercebackoffice.dto;

import com.ecommercebackoffice.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderCreateResponseDto {
    //속성
    private final Long id;
    private final OrderStatus orderStatus;
    private final Long totalPrice;
    private final String message;

    //생성자
    public OrderCreateResponseDto(Long id, OrderStatus orderStatus, Long totalPrice, String message) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.message = message;
    }

    //기능
}
