package com.ecommercebackoffice.controller;

import com.ecommercebackoffice.dto.CustomerDetailResponse;
import com.ecommercebackoffice.dto.CustomerListResponse;
//import com.ecommercebackoffice.dto.CustomerUpdateRequest; // 수정 DTO 임포트
import com.ecommercebackoffice.dto.CustomerStatusUpdateRequest;
import com.ecommercebackoffice.dto.CustomerUpdateRequest;
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
            @RequestParam(required = false) String keyword, // name -> keyword로 변경
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "customerName") String sortBy,
            @RequestParam(required = false) String customerStatus
    ) {
        CustomerListResponse response = customerService.getCustomers(keyword, page, size, sortBy, customerStatus);
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

    /**
     * 특정 고객 정보 수정 API
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDetailResponse> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerUpdateRequest request
    ) {
        CustomerDetailResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response); // 수정된 데이터와 함께 200 OK 응답
    }
    /**
     * 특정 고객 상태 변경 API
     */

    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomerDetailResponse> updateCustomerStatus(
            @PathVariable Long id,
            @RequestBody CustomerStatusUpdateRequest request
    ) {
        CustomerDetailResponse response = customerService.updateCustomerStatus(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 고객 삭제 API (소프트 삭제)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build(); // 성공 시 200 OK 응답
    }
}
