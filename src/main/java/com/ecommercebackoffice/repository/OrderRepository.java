package com.ecommercebackoffice.repository;

import com.ecommercebackoffice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
