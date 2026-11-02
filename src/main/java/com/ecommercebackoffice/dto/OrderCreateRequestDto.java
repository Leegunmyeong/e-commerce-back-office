package com.ecommercebackoffice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequestDto {
    //속성
    @NotNull(message = "고객 ID는 필수입니다.")
    private Long customerId;

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @NotNull(message = "관리자 ID는 필수입니다.")
    private Long adminId;

    @NotNull(message = "수량을 입력하세요.")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private Long orderQuantity;


    //생성자



    //기능
}
