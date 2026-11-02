package com.ecommercebackoffice.service;

import java.util.List;
import com.ecommercebackoffice.dto.CustomerStatusUpdateRequest;
import com.ecommercebackoffice.entity.Customer;
import com.ecommercebackoffice.dto.CustomerListResponse;
import com.ecommercebackoffice.dto.CustomerDetailResponse;
import com.ecommercebackoffice.dto.CustomerUpdateRequest;
import com.ecommercebackoffice.exception.ServiceException;
import com.ecommercebackoffice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

     /**
      findByCustomerNameContainingAndIsDeleteFalse -> 쿼리 메서드인 jpql부분을 씀으로써 메서드 이름을 줄임**
      고객 목록 및 검색 조회
     **/
     /** 화이트 리스트 구현 **/
     public CustomerListResponse getCustomers(String name, int page, int size, String sortBy, String customerStatus) {
         if (sortBy == null || sortBy.isBlank()) {
             sortBy = "customerRegisteredAt";
         }
         List<String> allowedSortFields = List.of("customerName", "customerRegisteredAt", "id");
         if (!allowedSortFields.contains(sortBy)) {
             throw new ServiceException(HttpStatus.BAD_REQUEST, "지원하지 않는 정렬 기준입니다: " + sortBy);
         }
         Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy).ascending());
         Page<Customer> customerPage;

         if (StringUtils.hasText(name)) {
             customerPage = customerRepository.findCustomers(name, pageable);
         } else {
             customerPage = customerRepository.findAllActive(pageable);
         }

         return new CustomerListResponse(customerPage);
     }

    /**
     * 특정 고객 상세 조회
     */
    public CustomerDetailResponse getCustomerDetail(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "해당 고객을 찾을 수 없습니다."));

        if (customer.getIsDelete()) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "이미 삭제된 고객입니다.");
        }

        return new CustomerDetailResponse(
                customer.getId(),
                customer.getCustomerName(),
                customer.getCustomerEmail(),
                customer.getPhoneNumber(),
                customer.getCustomerStatus(),
                customer.getCustomerRegisteredAt()
        );
    }

    /**
     * 특정 고객 정보 수정
     */
    @Transactional
    public CustomerDetailResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "해당 고객을 찾을 수 없습니다."));

        if (customer.getIsDelete()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "삭제된 고객의 정보는 수정할 수 없습니다.");
        }

        // 정보 수정 수행
        customer.updateCustomerInfo(request.getName(), request.getEmail(), request.getPhone());

        // 수정된 최신 정보를 담은 DTO 반환
        return new CustomerDetailResponse(
                customer.getId(),
                customer.getCustomerName(),
                customer.getCustomerEmail(),
                customer.getPhoneNumber(),
                customer.getCustomerStatus(),
                customer.getCustomerRegisteredAt()
        );
    }
    /**
     * 특정 고객 상태 변경
     */
    @Transactional
    public CustomerDetailResponse updateCustomerStatus(Long id, CustomerStatusUpdateRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "해당 고객을 찾을 수 없습니다."));

        if (customer.getIsDelete()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "삭제된 고객의 상태는 변경할 수 없습니다.");
        }

        // 활성/비활성 값이 제대로 들어왔는지 자바 코드로 직접 체크!
        if (!"ACTIVE".equals(request.getStatus()) && !"INACTIVE".equals(request.getStatus())) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "상태값은 ACTIVE 또는 INACTIVE만 가능합니다.");
        }

        // 상태 변경 수행
        customer.updateCustomerStatus(request.getStatus());

        return new CustomerDetailResponse(
                customer.getId(),
                customer.getCustomerName(),
                customer.getCustomerEmail(),
                customer.getPhoneNumber(),
                customer.getCustomerStatus(),
                customer.getCustomerRegisteredAt()
        );
    }
    /**
     * 특정 고객 소프트 삭제
     */
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "해당 고객을 찾을 수 없습니다."));

        if (customer.getIsDelete()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "이미 삭제된 고객입니다.");
        }

        // 소프트 삭제 수행 (isDelete = true로 변경)
        customer.deleteCustomer();
    }

}