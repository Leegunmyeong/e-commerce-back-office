package com.ecommercebackoffice.dto;

import com.ecommercebackoffice.entity.ProductStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductCreateRequestDto {

    @NotNull(message = "관리자ID는 필수 입니다.")
    private Long adminId;
    // null, 빈 문자열, 공백만 입력하는 경우 방지
    @NotBlank(message = "상품명은 필수 입니다.")
    private String itemName;
    // null, 빈 문자열, 공백만 입력하는 경우 방지
    @NotBlank(message = "상품의 카테고리를 입력해주세요.")
    private String itemCategory;
    // null 방지
    @NotNull(message = "상품 가격을 입력해주세요.")
    // 값이 0이상인지 확인
    @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
    private Long itemPrice;
    // null 방지
    @NotNull(message = "상품의 수량을 적어주세요")
    @Min(value = 0, message = "상품 재고는 0개 이상이어야 합니다.")
    private Long itemQuantity;
    @NotNull(message = "단종 여부를 선택해주세요.")
    // true -> 단종 상품, false -> 단종이 아닌 상품
    private Boolean itemDiscontinuedStatus;
}
