package com.phuclinh.rag_chatbot.repository.impl;

import com.phuclinh.rag_chatbot.entity.ChatLog;
import com.phuclinh.rag_chatbot.repository.ChatLogRepositoryCustom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ChatLogRepositoryCustomImpl implements ChatLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ChatLog> findRecentChatLogsByKeyword(String keyword, Long departmentId, int limit) {
        String jpql = """
            SELECT c FROM ChatLog c
            WHERE c.department.id = :deptId
              AND LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY c.timestamp DESC
            """;

        TypedQuery<ChatLog> query = entityManager.createQuery(jpql, ChatLog.class);
        query.setParameter("deptId", departmentId);
        query.setParameter("keyword", keyword);
        query.setMaxResults(limit);

        return query.getResultList();
    }
}
