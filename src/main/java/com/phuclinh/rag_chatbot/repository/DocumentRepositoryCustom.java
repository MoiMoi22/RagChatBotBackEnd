package com.phuclinh.rag_chatbot.repository;

import com.phuclinh.rag_chatbot.entity.Document;

import java.util.List;

public interface DocumentRepositoryCustom {
    List<Document> findAllVisibleByUser(String username);  // ví dụ: lọc theo role & phòng ban
}
