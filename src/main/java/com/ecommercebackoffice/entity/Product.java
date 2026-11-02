package com.ecommercebackoffice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.YesNoConverter;

// is_delete 값에 Y와 N중 어떤 값을 지정해 줄건지 결정해주는 의미 및 용도
// 진짜 삭제가 아닌 데이터 테이블 명세서에 is_delete 값이 추가된다
// 이 어노테이션 사용 시 영구 삭제가 아닌 삭제 한 것이다라고 명시만 해놓은 것
// 삭제한 첫 하는 용도
@SoftDelete(columnName = "is_delete", converter = YesNoConverter.class)
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
    // 상품 수량(재고)
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
        // 상품을 등록한 관리자 저장
        this.admin = admin;
        // 사용자가 단종상태를 입력하고 단종이면 단종으로 상태 처리 그게 아닌 재고가 0이면 품절 상태 아니면 판매 상태
        // 상품 상태 자동 결정
        updateStockStatus();
    }

    // Service에서 전달받은 보낸 상품 수정 요청 처리
    public void updatedProductInfo(String itemName, String itemCategory, Long itemPrice) {
        // 상품명을 입력했다면 상품명 변경
        if (itemName != null) {
            this.itemName = itemName;
        }
        // 상품 카테고리를 입력했다면 상품 카테고리 변경
        if (itemCategory != null) {
            this.itemCategory = itemCategory;
        }
        // 상품 가격을 입력했다면 상품 가격 변경
        if (itemPrice != null) {
            this.itemPrice = itemPrice;
        }
    }


    // 상품 재고 수량 변경
    public void updateProductQuantity(Long itemQuantity) {
        // 1. 전달받은 수량으로 상품 재고 변경
        this.itemQuantity = itemQuantity;
        // 2. 변경된 재고 수량에 따라 상품 상태 자동 변경
        updateStockStatus();
    }

    // 재고 수량에 따라 상품 상태를 자동으로 변경하는 메서드
    private void updateStockStatus() {
        // 단종된 상품이라면 재고와 관계없이 단종 상태 유지
        if (this.itemDiscontinuedStatus) {
            this.itemStockStatus = ProductStatus.DISC;
        }
        // 재고가 0개라면 품정 상태로 변경
        else if (this.itemQuantity == 0) {
            this.itemStockStatus = ProductStatus.SOLD_OUT;
        }
        // 재고가 1개 이상이라면 판매 중 상태로 변경
        else {
            this.itemStockStatus = ProductStatus.SALE;
        }
    }

    // 상품 단종 여부 변경
    public void updateDiscontinuedStatus(Boolean itemDiscontinuedStatus) {
        // 1. 전달받은 값으로 상품 단종 여부 변경
        this.itemDiscontinuedStatus = itemDiscontinuedStatus;
        // 2. 변경된 단종 여부와 현재 재고 수량을 기준으로 상품 상태 자동 결정
        updateStockStatus();
    }
}
