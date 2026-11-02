package com.ecommercebackoffice.dto;

import lombok.Getter;

@Getter
public class ProductUpdatedInfoResponseDto {
    private final Long id;
    private final String itemName;
    private final String itemCategory;
    private final Long itemPrice;

    public ProductUpdatedInfoResponseDto(Long id, String itemName, String itemCategory, Long itemPrice) {
        this.id = id;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemPrice = itemPrice;
    }
}
