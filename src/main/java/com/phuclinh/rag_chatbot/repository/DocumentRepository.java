package com.phuclinh.rag_chatbot.repository;

import com.phuclinh.rag_chatbot.entity.Document;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, DocumentRepositoryCustom {
    List<Document> findByIdIn(List<Long> ids);
}
