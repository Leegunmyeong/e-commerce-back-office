package com.ecommercebackoffice.repository;

import com.ecommercebackoffice.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    // 관리자 이메일 중복 확인
    boolean existsByAdminEmail(String adminEmail);

    // 이메일로 관리자 정보 조회
    Optional<Admin> findByAdminEmail(String adminEmail);
}