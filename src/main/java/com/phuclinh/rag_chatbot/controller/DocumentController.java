package com.phuclinh.rag_chatbot.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.phuclinh.rag_chatbot.dto.ApiResponseDTO;
import com.phuclinh.rag_chatbot.service.DocumentService;

@Controller
@RequestMapping("/api/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file,
                                        @RequestParam Long departmentId,
                                        Authentication authentication) throws IOException {
        String username = authentication.getName();
        String fileUrl = documentService.uploadDocument(file, username, departmentId);
        String message = "Tải lên thành công: " + fileUrl;
        return ResponseEntity.ok(new ApiResponseDTO(200, message));
    }


}

