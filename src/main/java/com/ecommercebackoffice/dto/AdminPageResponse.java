package com.ecommercebackoffice.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AdminPageResponse {

    // 속성
    private int currentPage;
    private int pageSize;
    private long totalCount;
    private int totalPage;
    private List<AdminListResponse> adminList;

    // 생성자

    public AdminPageResponse(int currentPage, int pageSize,
                             long totalCount, int totalPage,
                             List<AdminListResponse> adminList) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.adminList = adminList;
    }
}