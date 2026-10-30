package com.ecommercebackoffice.controller;


import com.ecommercebackoffice.dto.GetProductDetailResponse;
import com.ecommercebackoffice.dto.ProductCreateRequestDto;
import com.ecommercebackoffice.dto.ProductCreateResponseDto;
import com.ecommercebackoffice.dto.ProductListGetResponse;
import com.ecommercebackoffice.entity.ProductStatus;
import com.ecommercebackoffice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/products")
@RequiredArgsConstructor
@RestController
public class Productcontroller {
    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductCreateResponseDto> productCreated(@Valid @RequestBody ProductCreateRequestDto requestDto) {
        ProductCreateResponseDto responseDto = productService.productCreated(requestDto);
        ResponseEntity<ProductCreateResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        return response;
    }

    // 상품 리스트 조회
    @GetMapping
    public ResponseEntity<ProductListGetResponse> getProductList(
            @RequestParam(name = "item-name", required = false) String itemName,
            @RequestParam(name = "page-number", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "status", defaultValue = "SALE") ProductStatus status
    ) {
        ProductListGetResponse responseDto = productService.getProductList(itemName, page, size, filter, sort, category, status);
        ResponseEntity<ProductListGetResponse> response = new ResponseEntity<>(responseDto, HttpStatus.OK);
        return response;
    }


    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<GetProductDetailResponse> getProductDetail (@PathVariable Long productId) {
        GetProductDetailResponse responseDto = productService.getProductDetail(productId);
        ResponseEntity<GetProductDetailResponse> response = new ResponseEntity<>(responseDto, HttpStatus.OK);
        return response;
    }

}
