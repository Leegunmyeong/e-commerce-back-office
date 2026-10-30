package com.ecommercebackoffice.dto;

import com.ecommercebackoffice.entity.ProductStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ProductCreateResponseDto {
    private final String adminName;
    private final Long id;
    private final String itemName;
    private final String itemCategory;
    private final Long itemPrice;
    private final Long itemQuantity;
    private final ProductStatus itemStockStatus;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime itemRegistrationAt;
    private final String adminEmail;


    public ProductCreateResponseDto(String adminName, String adminEmail, Long id, String itemName, String itemCategory, Long itemPrice, Long itemQuantity ,ProductStatus itemStockStatus, LocalDateTime itemRegistrationAt) {
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.id = id;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemStockStatus = itemStockStatus;
        this.itemRegistrationAt = itemRegistrationAt;
    }
}
