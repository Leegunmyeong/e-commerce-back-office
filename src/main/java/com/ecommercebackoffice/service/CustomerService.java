package com.ecommercebackoffice.service;

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
     * jpql** -> findByCustomerNameContainingAndIsDeleteFalse
     * /**
     * 고객 목록 및 검색 조회
     */
    public CustomerListResponse getCustomers(String name, int page, int size, String sortBy, String customerStatus) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy).ascending());
        Page<Customer> customerPage;
        // name이 있으면 이름 검색, 없으면 전체 조회
        if (StringUtils.hasText(name)) {
            customerPage = customerRepository.findByCustomerNameContainingAndIsDeleteFalse(name, pageable);
        } else {
            customerPage = customerRepository.findByIsDeleteFalse(pageable);
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
}
    /**
     * 특정 고객 정보 수정
     */
//    @Transactional
//    public void updateCustomer(Long id, CustomerUpdateRequest request) {
//        Customer customer = customerRepository.findById(id)
//                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "해당 고객을 찾을 수 없습니다."));
//
//        if (customer.getIsDelete()) {
//            throw new ServiceException(HttpStatus.BAD_REQUEST, "삭제된 고객의 정보는 수정할 수 없습니다.");
//        }
//
//        customer.updateCustomerInfo(request.getName(), request.getEmail(), request.getPhone());
//    }
//}