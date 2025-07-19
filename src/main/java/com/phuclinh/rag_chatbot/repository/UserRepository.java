package com.phuclinh.rag_chatbot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phuclinh.rag_chatbot.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByUsername(String username); // Đăng nhập
    Optional<User> findByEmail(String email); // Cập nhật thông tin
    Boolean existsByEmail(String mail); // Quên mật khẩu
    Boolean existsByUsername(String username);
    void deleteById(Long id); // đã có sẵn
}

// 