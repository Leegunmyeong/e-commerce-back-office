package com.ecommercebackoffice.repository;

import com.ecommercebackoffice.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // 이름에 특정 키워드가 포함되어 있고, 삭제되지 않은 고객 조회 (페이징 지원)
    Page<Customer> findByCustomerNameContainingAndIsDeleteFalse(String keyword, Pageable pageable);
    // 전체 목록 중 삭제되지 않은 고객 조회 (검색어 없을 때)
    Page<Customer> findByIsDeleteFalse(Pageable pageable);
}