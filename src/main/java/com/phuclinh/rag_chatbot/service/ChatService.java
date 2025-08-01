package com.phuclinh.rag_chatbot.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.phuclinh.rag_chatbot.dto.ChatMessageResponseDTO;
import com.phuclinh.rag_chatbot.dto.RagApiResponse;
import com.phuclinh.rag_chatbot.entity.ChatLog;
import com.phuclinh.rag_chatbot.entity.Document;
import com.phuclinh.rag_chatbot.entity.User;
import com.phuclinh.rag_chatbot.exception.ResourceNotFoundException;
import com.phuclinh.rag_chatbot.repository.ChatLogRepository;
import com.phuclinh.rag_chatbot.repository.DocumentRepository;
import com.phuclinh.rag_chatbot.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatService {

    @Autowired
    private ChatLogRepository chatLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public ChatMessageResponseDTO processMessage(String question, String username) {
        // 1. Gọi RAG API
        RagApiResponse ragResponse = callRagApi(question);

        // 2. Lấy các fileUrl từ documentId
        List<Long> docIds = ragResponse.getSourceDocuments().stream()
                .map(Long::valueOf)
                .toList();

        List<String> fileUrls = documentRepository.findByIdIn(docIds).stream()
                .map(Document::getFileUrl)
                .toList();

        // 3. Lấy thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();

        // 4. Lưu chat log
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        ChatLog chatLog = new ChatLog();
        chatLog.setQuestion(question);
        chatLog.setAnswer(ragResponse.getAnswer());
        chatLog.setSource_documents(String.join("\n", fileUrls));
        chatLog.setUser(user);
        chatLog.setDepartment(user.getDepartment());
        chatLog.setCreatedAt(now);

        chatLogRepository.save(chatLog);

        // 5. Trả về response cho client
        return new ChatMessageResponseDTO(
                ragResponse.getAnswer(),
                fileUrls,
                now
        );
    }

    private RagApiResponse callRagApi(String question) {
        String apiUrl = "http://localhost:8000/ask";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> request = Map.of("question", question);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<RagApiResponse> response = restTemplate.postForEntity(apiUrl, entity, RagApiResponse.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Không nhận được phản hồi hợp lệ từ RAG server.");
        }

        return response.getBody();
    }
}