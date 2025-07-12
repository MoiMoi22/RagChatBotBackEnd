package com.phuclinh.rag_chatbot.repository;

import com.phuclinh.rag_chatbot.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepo extends JpaRepository<Department, Long> {
}
