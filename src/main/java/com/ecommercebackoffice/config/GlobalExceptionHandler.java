package com.ecommercebackoffice.config;

import com.ecommercebackoffice.dto.ErrorResponse;
import com.ecommercebackoffice.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 애플리케이션에서 발생하는 공통 예외 처리
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ServiceException과 이를 상속한 예외를 처리
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(response, ex.getStatus());
        }
}
