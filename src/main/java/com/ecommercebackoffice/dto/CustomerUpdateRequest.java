package com.ecommercebackoffice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerUpdateRequest {
    private String customerName;
    private String customerEmail;
    private String phoneNumber;
}
