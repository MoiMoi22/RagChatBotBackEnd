package com.phuclinh.rag_chatbot.repository;

import com.phuclinh.rag_chatbot.entity.ChatLog;

import java.util.List;

public interface ChatLogRepositoryCustom {
    List<ChatLog> findRecentChatLogsByKeyword(String keyword, Long departmentId, int limit);
}