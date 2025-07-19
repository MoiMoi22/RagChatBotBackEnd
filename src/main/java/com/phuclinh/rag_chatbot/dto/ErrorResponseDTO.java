package com.phuclinh.rag_chatbot.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
    private int status;
    private String error;
    private Object message; // 🔄 đổi từ String sang Map
    private String path;
    private LocalDateTime timestamp;
}