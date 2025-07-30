package com.phuclinh.rag_chatbot.repository;

import com.phuclinh.rag_chatbot.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, DocumentRepositoryCustom {
    // Có thể thêm query đơn giản nếu cần, ví dụ:
    // List<Document> findByDepartmentId(Long departmentId);
}
