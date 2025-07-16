package com.phuclinh.rag_chatbot.repository;

import java.util.List;
import java.util.Optional;

import com.phuclinh.rag_chatbot.entity.Document;

public interface DocumentRepository {
    List<Document> findAllDocuments(); // Liệt kê tất cả tài liệu (Phát triển thêm liệt kê theo phòng ban)
    Optional<Document> getDocument(String file);// Cần nghiên cứu thêm
    void addDocument(Document doc); // Thêm tài liệu
    void updateDocument(Document doc); // Cập nhật tài liệu
    void deleteDocument(); // Phát triển sau
}

// Cần review lại để đưa ra phương án giải quyết
