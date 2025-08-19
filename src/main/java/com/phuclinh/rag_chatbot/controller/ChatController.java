package com.phuclinh.rag_chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.phuclinh.rag_chatbot.dto.ChatMessageRequestDTO;
import com.phuclinh.rag_chatbot.dto.ChatMessageResponseDTO;
import com.phuclinh.rag_chatbot.exception.InvalidTokenException;
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
    @GetMapping("/login")
    public String getMethodName1() {
        return "login";
    }
    @GetMapping("/upload-demo")
    public String uploadFile() {
        return "upload";
    }
    
   @MessageMapping("/chat")
public void sendMessage(@Valid @Payload ChatMessageRequestDTO message, Authentication auth) {
    if (auth == null) {
       throw new InvalidTokenException("Token không hợp lệ hoặc đã hết hạn");
    }

    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
    String username = userDetails.getUsername();

    System.out.println("Nhận message từ " + username);

    ChatMessageResponseDTO response = chatService.processMessage(message.getQuestion(), username);
    System.out.println(response.sourceAsString());

//     ChatMessageResponseDTO tempResponse = new ChatMessageResponseDTO(
//     "Đây là câu trả lời từ chatbot",
//     List.of("https://res.cloudinary.com/dadyh5hjx/raw/upload/v1754037304/mllyckal2miv5xq3eqop.pdf", "Nguồn 2", "Nguồn 3"),
//     LocalDateTime.now()
// );
    messagingTemplate.convertAndSendToUser(
        username,
        "/queue/messages",
        response
        //  tempResponse
    );
}

}
