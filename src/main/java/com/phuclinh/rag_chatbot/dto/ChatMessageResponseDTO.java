package com.phuclinh.rag_chatbot.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDTO {
    private String answer;
    private List<String> sourceDocuments;
    private LocalDateTime timestamp; // LocalDateTime để đồng bộ với ChatLog

    public String sourceAsString() {
        if(sourceDocuments == null){
            return "Không có doc đi kèm";
        }
        return String.join("\n", sourceDocuments);
    }
}

