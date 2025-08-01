package com.phuclinh.rag_chatbot.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DocumentUploadRequestDTO {
    private String title;
    private String fileName;
    private Long departmentId;
}
