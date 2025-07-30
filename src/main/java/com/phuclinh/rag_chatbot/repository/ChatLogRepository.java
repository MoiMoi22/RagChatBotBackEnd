package com.phuclinh.rag_chatbot.repository;

import com.phuclinh.rag_chatbot.entity.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long>, ChatLogRepositoryCustom {

    List<ChatLog> findAll(); // kế thừa sẵn từ JpaRepository

    // Tùy theo thiết kế entity, bạn có thể dùng tên cột thực tế
    Optional<ChatLog> findByUserIdAndDepartmentId(Long userId, Long departmentId); 
}

