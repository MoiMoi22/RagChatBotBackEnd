package com.phuclinh.rag_chatbot.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

     private Long id;

    private String username;

    private String fullName;

    private String email;

    private Long roleId; // hoặc roleId tùy use case

    private Long departmentId; // hoặc departmentId

    private LocalDateTime createdAt;
    
}
