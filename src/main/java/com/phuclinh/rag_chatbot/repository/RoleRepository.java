package com.phuclinh.rag_chatbot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phuclinh.rag_chatbot.entity.Role;

// Chưa quá quan trọng
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findById(Long id);
    void deleteById(Long id);   // Xóa role
} 

// Hiện tại chỉ cần mỗi liệt kê và (add)
