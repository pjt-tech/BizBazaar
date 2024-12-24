package com.commerce.BizBazaar.user.service;

import com.commerce.BizBazaar.user.entity.Customer;
import com.commerce.BizBazaar.user.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 모든 고객 조회
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // 고객 ID로 조회
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    // 고객 추가
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // 고객 수정
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getCustomerById(id);
        customer.setName(customerDetails.getName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhoneNumber(customerDetails.getPhoneNumber());
        return customerRepository.save(customer);
    }

    // 고객 삭제
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
