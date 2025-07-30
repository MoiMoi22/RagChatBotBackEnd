package com.phuclinh.rag_chatbot.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import com.phuclinh.rag_chatbot.dto.ChatMessageRequestDTO;
import com.phuclinh.rag_chatbot.dto.ChatMessageResponseDTO;
import com.phuclinh.rag_chatbot.security.CustomUserDetails;
import com.phuclinh.rag_chatbot.service.ChatService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatService chatService;

    @GetMapping("/chat-demo")
    public String getMethodName() {
        return "chat";
    }
    @GetMapping("/upload-demo")
    public String uploadFile() {
        return "upload";
    }
    
    @MessageMapping("/chat")
    public void sendMessage(@Valid @Payload ChatMessageRequestDTO message, Authentication auth) {
        System.out.println("Nhận được message từ client");

        if (auth != null) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            String username = userDetails.getUsername();

            System.out.println("Sending to user: " + username + "/// " + auth.getName());

            // Gọi RAG API
            ChatMessageResponseDTO res = callRagApi(message.getQuestion());

            // Gán thời gian chung
            LocalDateTime now = LocalDateTime.now();

            // Lưu ChatLog
            chatService.saveChatLog(
                message.getQuestion(),
                res.getAnswer(),
                res.sourceAsString(),
                username,
                now
            );

            // Gửi lại cho client
            messagingTemplate.convertAndSendToUser(
                username,
                "/queue/messages",
                new ChatMessageResponseDTO(
                    res.getAnswer(),
                    res.getSourceDocuments(),
                    now // gắn luôn vào response
                )
            );
        } else {
            System.out.println("Auth null");
        }
    }




private ChatMessageResponseDTO callRagApi(String question) {
    RestTemplate rest = new RestTemplate();
    String apiUrl = "http://localhost:8000/ask";

    Map<String, String> request = Map.of("question", question);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

    ResponseEntity<ChatMessageResponseDTO> response = rest.postForEntity(apiUrl, entity, ChatMessageResponseDTO.class);

    return response.getBody();
}


}
