package com.ecommercebackoffice.dto;

import com.ecommercebackoffice.entity.ProductStatus;
import lombok.Getter;

@Getter
public class UpdatedProductStockQuantityResponseDto {
    private final Long id;
    private final String itemName;
    private final Long itemQuantity;
    private final ProductStatus itemStockStatus;

    public UpdatedProductStockQuantityResponseDto(Long id, String itemName, Long itemQuantity, ProductStatus itemStockStatus) {
        this.id = id;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemStockStatus = itemStockStatus;
    }
}
