package com.commerce.BizBazaar.user.repository;

import com.commerce.BizBazaar.user.entity.Customer;
import com.commerce.BizBazaar.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
