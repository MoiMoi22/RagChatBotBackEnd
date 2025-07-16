package com.phuclinh.rag_chatbot.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO {
    private int status;
    private String message;
}
