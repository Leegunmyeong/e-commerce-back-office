package com.ecommercebackoffice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; // 1. 임포트 추가

@Getter
@Setter // 2. 이 부분을 추가해주어야 JSON 값을 필드에 꽂아넣을 수 있습니다!
@NoArgsConstructor
public class CustomerUpdateRequest {

    private String name;
    private String email;
    private String phone;
}