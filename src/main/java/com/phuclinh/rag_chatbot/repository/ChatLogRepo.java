package com.phuclinh.rag_chatbot.repository;

import com.phuclinh.rag_chatbot.entity.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatLogRepo extends JpaRepository<ChatLog, Long> {
}
