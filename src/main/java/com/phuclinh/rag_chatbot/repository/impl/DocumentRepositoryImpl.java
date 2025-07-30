package com.phuclinh.rag_chatbot.repository.impl;

import com.phuclinh.rag_chatbot.entity.Document;
import com.phuclinh.rag_chatbot.repository.DocumentRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentRepositoryImpl implements DocumentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Document> findAllVisibleByUser(String username) {
        // Ví dụ: chỉ admin được xem tất cả, manager thì lọc theo phòng ban
        String jpql = "SELECT d FROM Document d WHERE d.uploadedBy.username = :username OR d.department.id IN " +
                      "(SELECT u.department.id FROM User u WHERE u.username = :username)";
        TypedQuery<Document> query = entityManager.createQuery(jpql, Document.class);
        query.setParameter("username", username);
        return query.getResultList();
    }
}
