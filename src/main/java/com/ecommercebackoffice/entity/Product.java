package com.ecommercebackoffice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@SoftDelete(columnName = "is_delete")
@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends ProductBaseEntity {
    // 상품 고유 식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 관리자 고유 식별자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;
    // 상품명
    @Column(name = "item_name", nullable = false)
    private String itemName;
    // 상품 카테고리
    @Column(name = "item_category", nullable = false, length = 20)
    private String itemCategory;
    // 상품 가격
    @Column(name = "item_price", nullable = false)
    private Long itemPrice;
    // 상품 수량
    @Column(name = "item_quantity", nullable = false)
    private Long itemQuantity;
    // 상품 재고 상태
    // Enum 이름을 DB에 문자열로 저장
    @Enumerated(EnumType.STRING)
    @Column(name = "item_stock_status", length = 20, nullable = false)
    private ProductStatus itemStockStatus;
    // 상품 단종 여부
    @Column(name = "item_Discontinued_status", nullable = false)
    private Boolean itemDiscontinuedStatus;


    public Product(String itemName, String itemCategory, Long itemPrice, Long itemQuantity, Boolean itemDiscontinuedStatus, Admin admin) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemDiscontinuedStatus = itemDiscontinuedStatus;
        // 사용자가 단종상태를 입력하고 단종이면 단종으로 상태 처리 그게 아닌 재고가 0이면 품절 상태 아니면 판매 상태
        // 상품 상태 자동 결정
        if (itemDiscontinuedStatus) {
            this.itemStockStatus = ProductStatus.DISC;
        } else if (itemQuantity == 0) {
            this.itemStockStatus = ProductStatus.SOLD_OUT;
        } else {
            this.itemStockStatus = ProductStatus.SALE;
        }
        // 상품을 등록한 관리자 저장
        this.admin = admin;
    }





}
