package com.phuclinh.rag_chatbot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.phuclinh.rag_chatbot.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.username = :username")
    Optional<User> findByUsernameWithRole(@Param("username") String username);
    Optional<User> findByUsername(String username); // Đăng nhập
    Optional<User> findByEmail(String email); // Cập nhật thông tin
    Boolean existsByEmail(String mail); // Quên mật khẩu
    Boolean existsByUsername(String username);
    void deleteById(Long id); // đã có sẵn
}

// 