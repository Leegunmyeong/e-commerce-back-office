package com.ecommercebackoffice.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductListGetResponse {
    private final Long currentPage;
    private final Long pageSize;
    private final Long totalCount;
    private final Long totalPage;
    private final List<ProductDto> productList;

    public ProductListGetResponse(Long currentPage, Long pageSize, Long totalCount, Long totalPage, List<ProductDto> productList) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.productList = productList;
    }
}
