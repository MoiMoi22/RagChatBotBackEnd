package com.phuclinh.rag_chatbot.dto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RagApiResponse {
    private String answer;
    private List<Integer> sourceDocuments;
}
