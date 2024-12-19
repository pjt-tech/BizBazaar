package com.commerce.BizBazaar.user.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String username;
    private String role;

    public AuthResponseDto(String username, String role) {
        this.username = username;
        this.role = role;
    }
}