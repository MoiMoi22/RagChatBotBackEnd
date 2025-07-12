package com.phuclinh.rag_chatbot.repository.dao;

import java.util.List;

import com.phuclinh.rag_chatbot.entity.ChatLog;

public interface ChatLogDao {
    List<ChatLog> findAllChatLogs();
    ChatLog geChatLog(String userId, String departmentId); // Xem lại lịch sử chat của user
    void addChatLog(ChatLog chatLog); // Tạo chat log mới
    void updateChatLog(ChatLog chatLog); // Có thể không cần
    void deleteChatLog(); // Có thể không cần
}

// ==> Nằm trong thành phần cơ bản cần được hoàn thiện
