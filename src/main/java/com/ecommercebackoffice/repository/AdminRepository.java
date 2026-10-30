package com.ecommercebackoffice.repository;

import com.ecommercebackoffice.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    // 관리자 이메일 중복 확인
    boolean existsByAdminEmail(String adminEmail);

    // 이메일로 관리자 정보 조회
    Optional<Admin> findByAdminEmail(String adminEmail);

    // 관리자 이름 부분 일치 검색
    Page<Admin> findByAdminNameContaining(String adminName, Pageable pageable);

    // 관리자 역할별 조회
    Page<Admin> findByAdminRole(String adminRole, Pageable pageable);

    // 관리자 상태별 조회
    Page<Admin> findByAdminStatus(String adminStatus, Pageable pageable);

    // 관리자 역할과 상태별 조회
    Page<Admin> findByAdminRoleAndAdminStatus(
            String adminRole, String adminStatus, Pageable pageable);
}