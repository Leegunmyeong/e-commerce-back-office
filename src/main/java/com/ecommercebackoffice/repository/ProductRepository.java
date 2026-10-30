package com.ecommercebackoffice.repository;

import com.ecommercebackoffice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
