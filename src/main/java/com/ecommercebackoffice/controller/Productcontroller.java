package com.ecommercebackoffice.controller;


import com.ecommercebackoffice.dto.*;
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
    public ResponseEntity<ProductCreateResponseDto> createdProduct(@Valid @RequestBody ProductCreateRequestDto requestDto) {
        ProductCreateResponseDto responseDto = productService.createdProduct(requestDto);
        ResponseEntity<ProductCreateResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        return response;
    }

    // 상품 리스트 조회
    @GetMapping
    public ResponseEntity<ProductListGetResponse> getProductList(
            @RequestParam(name = "item-name", required = false) String itemName,
            @RequestParam(name = "page-number", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "filter", defaultValue = "price") String filter,
            @RequestParam(name = "sort", defaultValue = "asc") String sort,
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

    // 상품 정보 수정
    @PatchMapping("/{productId}/info")
    public ResponseEntity<ProductUpdatedInfoResponseDto> updatedProductInfo(@PathVariable Long productId, @Valid @RequestBody ProductUpdatedInfoRequestDto requestDto) {
        // Service에게 수정 요청 후 반환 값 Response Dto에 담기
        ProductUpdatedInfoResponseDto responseDto = productService.updatedProductInfo(productId, requestDto);
        // Response Dto와 200 OK 상태코드 결과 담기
        ResponseEntity<ProductUpdatedInfoResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);
        // 클라이언트에게 200 OK와 수정 결과 반환
        return response;
    }

    // 상품 재고 변경
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<UpdatedProductStockQuantityResponseDto> updatedProductStockQuantity(@PathVariable Long productId, @RequestBody UpdatedProductStockQuantityRequestDto requestdto) {
        // Service에게 재고 변경 요청 후 반환 값 Response Dto에 담기
        UpdatedProductStockQuantityResponseDto responseDto = productService.updatedProductStockQuantity(productId, requestdto);
        // Response Dto와 200 OK 상태코드 결과 담기
        ResponseEntity<UpdatedProductStockQuantityResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);
        // 클라이언트에게 200 OK와 변경된 결과 반환
        return response;
    }

    // 상품 상태 변경
    @PatchMapping("/{productId}/status")
    public ResponseEntity<UpdatedProductStockStatusResponseDto> updatedProductStockStatus(@PathVariable Long productId, @RequestBody UpdatedProductStockStatusRequestDto requestDto) {
        // Service에게 상품 상태 변경 요청 후 반환 값 Response DTO에 담기
        UpdatedProductStockStatusResponseDto responseDto = productService.updatedProductStockStatus(productId, requestDto);
        // Response Dto와 200 OK 상태코드 결과 담기
        ResponseEntity<UpdatedProductStockStatusResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);
        // 클라이언트에게 응답 DTO 반환
        return response;
    }
}
