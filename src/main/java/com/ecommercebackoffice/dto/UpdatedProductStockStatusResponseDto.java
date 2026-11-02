package com.ecommercebackoffice.dto;

import com.ecommercebackoffice.entity.ProductStatus;
import lombok.Getter;

@Getter
public class UpdatedProductStockStatusResponseDto {
    private final Long id;
    private final String itemName;
    private final Long itemQuantity;
    private final Boolean itemDiscontinuedStatus;
    private final ProductStatus itemStockStatus;

    public UpdatedProductStockStatusResponseDto(Long id, String itemName, Long itemQuantity, Boolean itemDiscontinuedStatus, ProductStatus itemStockStatus) {
        this.id = id;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemDiscontinuedStatus = itemDiscontinuedStatus;
        this.itemStockStatus = itemStockStatus;
    }
}
