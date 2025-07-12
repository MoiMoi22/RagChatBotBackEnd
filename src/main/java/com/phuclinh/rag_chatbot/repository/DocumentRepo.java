package com.phuclinh.rag_chatbot.repository;

import com.phuclinh.rag_chatbot.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepo  extends JpaRepository<Document, Long>{
}
