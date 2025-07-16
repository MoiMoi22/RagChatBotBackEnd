package com.phuclinh.rag_chatbot.repository;

import java.util.List;
import java.util.Optional;

import com.phuclinh.rag_chatbot.entity.ChatLog;

public interface ChatLogRepository {
    List<ChatLog> findAllChatLogs();
    Optional<ChatLog> geChatLog(String userId, String departmentId); // Xem lại lịch sử chat của user
    void addChatLog(ChatLog chatLog); // Tạo chat log mới
    void updateChatLog(ChatLog chatLog); // Có thể không cần
    void deleteChatLog(); // Có thể không cần
}

// ==> Nằm trong thành phần cơ bản cần được hoàn thiện
