package com.phuclinh.rag_chatbot.dto;

import com.phuclinh.rag_chatbot.validation.ValidUserStatus;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequestDTO {
    private String userName;
    private String fullName;

    @Email(message = "Email không hợp lệ")
    private String email;

    @ValidUserStatus // Custom annotation để kiểm tra status hợp lệ
    private String status;
}
