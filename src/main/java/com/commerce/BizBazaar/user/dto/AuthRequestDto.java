package com.commerce.BizBazaar.user.dto;

import lombok.Data;

@Data
public class AuthRequestDto {

    private String username;
    private String password;
    private String name;
    private String email;
}
