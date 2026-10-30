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
    private Long id;

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

    @Column(name = "customer_name", nullable = false, length = 40)
    private String customerName;

    @Column(name = "customer_email", nullable = false, unique = true, length = 40)
    private String customerEmail;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_quantity", nullable = false)
    private Long productQuantity;

    @Column(name = "product_price", nullable = false)
    private Long productPrice;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "order_status", nullable = false, length = 20)
    private String orderStatus;

    @Column(name = "admin_name", nullable = false, length = 20)
    private String adminName;

    @Column(name = "admin_email", nullable = false, unique = true, length = 40)
    private String adminEmail;

    @Column(name = "admin_role", nullable = false, length = 20)
    private String adminRole;

    //생성자

    public Order(Admin admin, Product product, Customer customer, String customerName,
                 String customerEmail, String productName, Long productQuantity, Long productPrice,
                 String adminName, String adminEmail, String adminRole
    ) {
        // FK 관계 매핑
        this.admin = admin;
        this.product = product;
        this.customer = customer;

        //고객 정보 스냅샷
        this.customerName = customerName;
        this.customerEmail = customerEmail;

        //제품 정보 스냅샷
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;

        // 주문일 시간 생성 및 상태 주입(초기 값 : 상품준비중)
        this.orderDate = LocalDateTime.now();
        this.orderStatus = "Item Preparation";

        //관리자 정보 스냅샷
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminRole = adminRole;

    }


}