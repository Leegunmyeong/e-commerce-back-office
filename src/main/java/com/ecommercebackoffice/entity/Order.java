package com.ecommercebackoffice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
public class Order {
    //속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //주문번호 자동 생성 값으로 활용

    //연관관계 매핑(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


    //고객 정보 값 저장
    @Column(name = "customer_name", nullable = false, length = 40)
    private String customerName;
    @Column(name = "customer_email", nullable = false, length = 40)
    private String customerEmail;

    //상품 정보 값 저장
    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;
    @Column(name = "product_price", nullable = false)
    private Long productPrice;

    //주문 정보 값 저장
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 20)
    private OrderStatus orderStatus;
    @Column(name = "order_quantity", nullable = false)
    private Long orderQuantity;
    @Column(name = "order_totalprice", nullable = false)
    private Long totalPrice;

    //관리자 정보 값 저장
    @Column(name = "admin_name", nullable = false, length = 20)
    private String adminName;
    @Column(name = "admin_email", nullable = false, length = 40)
    private String adminEmail;
    @Column(name = "admin_role", nullable = false, length = 20)
    private String adminRole;


    //생성자
    public Order(Admin admin, Product product, Customer customer, Long orderQuantity) {
        // FK 관계 매핑
        this.admin = admin;
        this.product = product;
        this.customer = customer;

        //고객 정보 스냅샷
        this.customerName = customer.getCustomerName();
        this.customerEmail = customer.getCustomerEmail();

        //제품 정보 스냅샷
        this.productName = product.getItemName();
        this.productPrice = product.getItemPrice();

        //주문 정보 스냅샷
        this.orderDate = LocalDateTime.now(); // 주문일 자동 생성
        this.orderStatus = OrderStatus.ITEM_PREPARATION; // 초기값 : 상품 준비중
        this.orderQuantity = orderQuantity; // 고객 주문한 수량
        this.totalPrice = getProductPrice() * orderQuantity;

        //관리자 정보 스냅샷
        this.adminName = admin.getAdminName();
        this.adminEmail = admin.getAdminEmail();
        this.adminRole = admin.getAdminRole();

    }

    /*
     *  기능
     *  1. 주문 수량 만큼 상품 재고 및 검증 및 차감 처리
     *  2. 재고 변경에 따른 상품 상태 자동 전환 처리
     *  3. 재고가 주문 수량보다 부족한 경우 주문 생성이 불가
     */



}