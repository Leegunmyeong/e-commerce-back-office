package com.ecommercebackoffice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProductUpdatedInfoRequestDto {

    // 수정할 상품명
    private String itemName;
    // 수정할 카테고리
    private String itemCategory;
    // 수정할 상품 가격 (선택값, 0원 이상)
    private Long itemPrice;
}
