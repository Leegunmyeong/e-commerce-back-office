package com.ecommercebackoffice.repository;

import com.ecommercebackoffice.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    //커스터머 엔티티 객체 조회 파라미터로 넘어온갚이 keyword가 포함되어 있는지 확인 후
    //isdelete 부분의 false인 부분을 데이터 필터링
    @Query("SELECT c FROM Customer c WHERE c.customerName LIKE %:keyword% AND c.isDelete = false")
    Page<Customer> findCustomers(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.isDelete = false")
    Page<Customer> findAllActive(Pageable pageable);
}