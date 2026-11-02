package com.ecommercebackoffice.service;

import com.ecommercebackoffice.dto.OrderCreateRequestDto;
import com.ecommercebackoffice.dto.OrderCreateResponseDto;
import com.ecommercebackoffice.entity.*;
import com.ecommercebackoffice.exception.ServiceException;
import com.ecommercebackoffice.repository.AdminRepository;
import com.ecommercebackoffice.repository.CustomerRepository;
import com.ecommercebackoffice.repository.OrderRepository;
import com.ecommercebackoffice.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    //속성
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final ProductService productService;


    //생성자
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
                        ProductRepository productRepository, AdminRepository adminRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.adminRepository = adminRepository;
        this.productService = productService;
    }

    //기능

    /*
     * 1. CS 주문 생성
     */
    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto) {

        // 1. 조회 (고객, 상품, 관리자 id 유무 확인 조회)
        Customer customer = customerRepository.findById(requestDto.getCustomerId()).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND, "존재하지 않는 고객입니다.")
        );
        Product product = productRepository.findById(requestDto.getProductId()).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다.")
        );
        Admin admin = adminRepository.findById(requestDto.getAdminId()).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND, "존재하지 않는 관리자입니다.")
        );

        // 2. 재고 수정 및 차감 처리 호출
        // 2-1. 재고 수정 및 차감처리 호출 (productService를 이용해서 기능 처리 해야함)
        // 예외 상황 상품이 단종이 경우, 상품이 품절인 경우, 재고가 주문 수량보다 부족한 경우 생성 불가
        productService.decreaseStock(product.getId(), requestDto.getOrderQuantity());

        // 3. 저장 값 준비 -> 저장 -> 저장 값 받기
        Order order = new Order(admin, product, customer, requestDto.getOrderQuantity());
        Order savedOrder = orderRepository.save(order);

        // 4. 반환 값 준비(주문번호, 총 금액, 주문 일, 주문 상태, 주문 성공 메시지)
        Long id = savedOrder.getId();
        OrderStatus orderStatus = savedOrder.getOrderStatus();
        Long totalPrice = savedOrder.getTotalPrice();
        String message = "주문이 성공적으로 생성되었습니다.";

        // 5. 반환
        OrderCreateResponseDto responseDto = new OrderCreateResponseDto(id, orderStatus, totalPrice, message);
        return responseDto;

    }

    // 2. 주문 리스트 조회
    public void getOrder(String keyword, OrderStatus status, int page, int size, String soryBy, String direction) {

        // 1. 정렬 방향 설정
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        /*
        * 2. 정렬 순서 기준(수량, 금액, 주문일)
        * 2-1. 만약에 수량이 기준이라면 수량 기준으로 정렬 방향 설정
        * 2-2. 만약에 금액이 기준이라면 금액 기준으로 정렬 방향 설정
        */



        
    }
}
