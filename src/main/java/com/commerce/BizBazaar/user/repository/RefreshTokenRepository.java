package com.commerce.BizBazaar.user.repository;

import com.commerce.BizBazaar.user.entity.RefreshToken;
import com.commerce.BizBazaar.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUsername(String username);
    void deleteByUsername(String username);
}
