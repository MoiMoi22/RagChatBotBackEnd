package com.phuclinh.rag_chatbot.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.phuclinh.rag_chatbot.dto.ApiResponseDTO;
import com.phuclinh.rag_chatbot.dto.DocumentUploadRequestDTO;
import com.phuclinh.rag_chatbot.service.DocumentService;

@Controller
@RequestMapping("/api/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestPart("metadata") DocumentUploadRequestDTO metadata,
            @RequestPart("file") MultipartFile file,
            Authentication authentication) throws IOException {

        String username = authentication.getName();

        String fileUrl = documentService.uploadDocument(
            file,
            metadata.getTitle(),
            metadata.getFileName(),
            username,
            metadata.getDepartmentId()
        );

        return ResponseEntity.ok(new ApiResponseDTO(200, "Tải lên thành công: " + fileUrl));
    }


}

