package com.commerce.BizBazaar.user.service;

import com.commerce.BizBazaar.user.entity.User;
import com.commerce.BizBazaar.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 모든 사용자 목록 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 사용자 ID로 조회
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 사용자 정보 업데이트
    public void updateUser(Long id, User user) {
        User existingUser = getUserById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        userRepository.save(existingUser);
    }

    // 사용자 삭제
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 사용자 추가
    public void createUser(User user) {
        userRepository.save(user);
    }
}
