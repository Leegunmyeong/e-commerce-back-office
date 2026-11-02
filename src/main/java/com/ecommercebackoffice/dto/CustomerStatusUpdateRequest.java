package com.ecommercebackoffice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerStatusUpdateRequest {
    private String status; // "활성" 또는 "비활성"
}