package com.commerce.BizBazaar.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;  // 회사명
    private String contactName;  // 연락처 이름
    private String email;        // 이메일
    private String phoneNumber;  // 전화번호
}
