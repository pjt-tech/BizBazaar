package com.commerce.BizBazaar.global.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private boolean success;  // 요청이 성공했는지 여부
    private String message;   // 메시지 (예: 성공, 실패 원인 등)
    private T data;           // 요청 결과 데이터 (예: 토큰, 사용자 정보 등)
    private int statusCode;   // HTTP 상태 코드

    // Constructor for success responses
    public ApiResponse(boolean success, String message, T data, int statusCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }

    // Constructor for error responses
    public ApiResponse(boolean success, String message, int statusCode) {
        this(success, message, null, statusCode);
    }
}

