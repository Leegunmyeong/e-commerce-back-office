package com.ecommercebackoffice.controller;

import com.ecommercebackoffice.dto.CustomerDetailResponse;
import com.ecommercebackoffice.dto.CustomerListResponse;
//import com.ecommercebackoffice.dto.CustomerUpdateRequest; // 수정 DTO 임포트
import com.ecommercebackoffice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 고객 목록 및 검색 조회 API (@RequestParam 명시 방식)
     */
    @GetMapping
    public ResponseEntity<CustomerListResponse> getCustomers(
            @RequestParam(required = false) String name, // keyword 대신 name으로 받기
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "customerName") String sortBy,
            @RequestParam(required = false) String customerStatus
    ) {
        CustomerListResponse response = customerService.getCustomers(name, page, size, sortBy, customerStatus);
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 고객 상세 조회 API
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailResponse> getCustomerDetail(@PathVariable Long id) {
        CustomerDetailResponse response = customerService.getCustomerDetail(id);
        return ResponseEntity.ok(response);
    }
}
    /**
     * 특정 고객 정보 수정 API
     */
//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateCustomer(
//            @PathVariable Long id,
//            @RequestBody CustomerUpdateRequest request
//    ) {
//        customerService.updateCustomer(id, request);
//        return ResponseEntity.ok().build();
//    }
//}