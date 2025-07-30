package com.phuclinh.rag_chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDTO {
    @NotBlank(message = "Câu hỏi không được để trống")
    private String question;
}
