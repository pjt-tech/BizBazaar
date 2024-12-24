package com.commerce.BizBazaar.user.service;

import com.commerce.BizBazaar.user.entity.Vendor;
import com.commerce.BizBazaar.user.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    @Autowired
    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    // 모든 벤더 조회
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // 벤더 ID로 조회
    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id " + id));
    }

    // 벤더 추가
    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    // 벤더 수정
    public Vendor updateVendor(Long id, Vendor vendorDetails) {
        Vendor vendor = getVendorById(id);
        vendor.setCompanyName(vendorDetails.getCompanyName());
        vendor.setContactName(vendorDetails.getContactName());
        vendor.setEmail(vendorDetails.getEmail());
        vendor.setPhoneNumber(vendorDetails.getPhoneNumber());
        return vendorRepository.save(vendor);
    }

    // 벤더 삭제
    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }
}