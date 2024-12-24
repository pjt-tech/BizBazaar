package com.commerce.BizBazaar.user.repository;

import com.commerce.BizBazaar.user.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
