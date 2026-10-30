package com.ecommercebackoffice.repository;


import com.ecommercebackoffice.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
