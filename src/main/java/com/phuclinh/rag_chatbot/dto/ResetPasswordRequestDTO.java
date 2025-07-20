package com.phuclinh.rag_chatbot.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequestDTO {
    private String token;
    private String newPassword;
}
