package com.phuclinh.rag_chatbot.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.phuclinh.rag_chatbot.exception.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private  Cloudinary cloudinary;

    private static final String[] ALLOWED_TYPES = {
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain",
            "application/vnd.oasis.opendocument.text"
    };

    public String uploadFile(MultipartFile file) throws IOException {
    try {
            String contentType = file.getContentType();
            if (!isAllowedType(contentType)) {
                throw new BadRequestException("Chỉ cho phép upload tài liệu văn bản (PDF, DOCX, TXT...)");
            }

            // Tạo file tạm
            File tempFile = File.createTempFile("upload-", ".docx");
            Files.write(tempFile.toPath(), file.getBytes());

            // Upload file tạm lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(
                    tempFile,
                    ObjectUtils.asMap(
                        "resource_type", "raw",
                        "unique_filename", true
                    )
            );

            tempFile.delete(); // Dọn file tạm nếu cần

            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl == null) {
                throw new BadRequestException("Không nhận được secure_url từ Cloudinary");
            }
            return secureUrl.toString();
    } catch (IOException e) {
        e.printStackTrace();
        throw new IOException("Lỗi khi đọc file hoặc upload lên Cloudinary", e);
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Upload thất bại: " + e.getMessage(), e);
    }
}



    private boolean isAllowedType(String contentType) {
        if (contentType == null) return false;
        for (String type : ALLOWED_TYPES) {
            if (contentType.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}
