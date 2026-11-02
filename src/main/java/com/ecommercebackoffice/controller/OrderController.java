package com.ecommercebackoffice.controller;


import com.ecommercebackoffice.dto.OrderCreateRequestDto;
import com.ecommercebackoffice.dto.OrderCreateResponseDto;
import com.ecommercebackoffice.entity.OrderStatus;
import com.ecommercebackoffice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    //속성
    private final OrderService orderService;


    //생성자
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //기능

    /*
     *  1. 주문생성
     *  1-1. 고객 정보, 상품 정보, 수량을 입력 받는다.
     *  1-2. 주문 등록 관리자 정보 저장(로그인 유저 == 주문 등록 관리자)을 위해 주문 등록 관리자 ID를 받는다.
     *  CS 대리 주문의 경우, 주문 소유자(고객)와 별도로 주문 등록 관리자 정보를 저장한다.
     */
    @PostMapping
    public ResponseEntity<OrderCreateResponseDto> createOrder(@RequestBody OrderCreateRequestDto requestDto) {

        // 1. 서비스 처리
        OrderCreateResponseDto responseDto = orderService.createOrder(requestDto);

        // 2. 응답 객체 생성
        ResponseEntity<OrderCreateResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        // 3. 반환
        return response;

    }
}
