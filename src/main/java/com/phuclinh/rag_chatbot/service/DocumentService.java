package com.phuclinh.rag_chatbot.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phuclinh.rag_chatbot.entity.Department;
import com.phuclinh.rag_chatbot.entity.Document;
import com.phuclinh.rag_chatbot.entity.User;
import com.phuclinh.rag_chatbot.exception.ResourceNotFoundException;
import com.phuclinh.rag_chatbot.repository.DepartmentRepository;
import com.phuclinh.rag_chatbot.repository.DocumentRepository;
import com.phuclinh.rag_chatbot.repository.UserRepository;

@Service
public class DocumentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;    

    public String uploadDocument(MultipartFile file, String username, Long departmentId) throws IOException {
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        String role = user.getRole().getName(); // giả định là "ADMIN", "MANAGER", "EMPLOYEE"

        // Kiểm tra quyền upload
        if (!role.equals("ADMIN") && !role.equals("MANAGER")) {
            throw new AccessDeniedException("Bạn không có quyền upload tài liệu.");
        }

        // MANAGER: chỉ được upload vào phòng ban của mình
        if (role.equals("MANAGER")) {
            Long userDeptId = user.getDepartment().getId();
            if (!userDeptId.equals(departmentId)) {
                throw new AccessDeniedException("Bạn không thể upload tài liệu vào phòng ban khác.");
            }
        }

        // Upload file lên Cloudinary
        String fileUrl = cloudinaryService.uploadFile(file);

        // Lấy phòng ban để gán
        Department department = departmentRepository.findById(departmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Phòng ban không tồn tại"));

        String originalName = file.getOriginalFilename();
        String nameWithoutExtension = originalName != null && originalName.contains(".")
            ? originalName.substring(0, originalName.lastIndexOf('.'))
            : originalName;

        // Lưu document
        Document doc = new Document();
        doc.setTitle(nameWithoutExtension); // Không có đuôi .docx
        doc.setFileName(nameWithoutExtension); // Không có đuôi
        doc.setFileType(file.getContentType()); // vẫn lưu MIME type: application/vnd.openxmlformats-officedocument.wordprocessingml.document
        doc.setFileUrl(fileUrl);
        doc.setUploadedBy(user);
        doc.setDepartment(department);

        documentRepository.save(doc);
        return fileUrl;
}

}
