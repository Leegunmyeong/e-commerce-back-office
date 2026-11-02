package com.ecommercebackoffice.config;

import com.ecommercebackoffice.dto.ErrorResponse;
import com.ecommercebackoffice.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    // 상품쪽에서 발생하는 예외처리
    // 요청 JSON 형식이나 자료형이 잘못되었을 때 예외 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handlerInvalidRequest(HttpMessageNotReadableException ex) {

        ErrorResponse errorResponse = new ErrorResponse("요청 데이터의 값 또는 자료형이 올바르지 않습니다.", HttpStatus.BAD_REQUEST.value());
        ResponseEntity<ErrorResponse> response = new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        return response;
    }
}
