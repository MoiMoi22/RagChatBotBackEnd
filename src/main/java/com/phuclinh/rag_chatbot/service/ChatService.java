package com.phuclinh.rag_chatbot.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phuclinh.rag_chatbot.entity.ChatLog;
import com.phuclinh.rag_chatbot.entity.User;
import com.phuclinh.rag_chatbot.exception.ResourceNotFoundException;
import com.phuclinh.rag_chatbot.repository.ChatLogRepository;
import com.phuclinh.rag_chatbot.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatService {
    @Autowired
    private ChatLogRepository chatLogRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveChatLog(String question, String answer, String sourceDocs, String username, LocalDateTime createdAt) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        ChatLog chatLog = new ChatLog();
        chatLog.setQuestion(question);
        chatLog.setAnswer(answer);
        chatLog.setSource_documents(sourceDocs);
        chatLog.setUser(user);
        chatLog.setDepartment(user.getDepartment());
        chatLog.setCreatedAt(createdAt); // dùng thời gian truyền vào

        chatLogRepository.save(chatLog);
    }
}
