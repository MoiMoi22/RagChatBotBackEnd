package com.phuclinh.rag_chatbot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phuclinh.rag_chatbot.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
    Optional<Department> findById(Long id); // Lấy thông tin 1 department (có thể chưa cần thiết)
    void deleteById(Long id); // Chưa cần thiết
}

// ==> Nằm trong thành phần cơ bản
