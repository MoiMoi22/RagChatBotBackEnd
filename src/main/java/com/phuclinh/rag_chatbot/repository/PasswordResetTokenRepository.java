package com.phuclinh.rag_chatbot.repository;

import java.util.List;

import com.phuclinh.rag_chatbot.entity.PasswordResetToken;

public interface PasswordResetTokenRepository {
   List<PasswordResetToken> findAllPasswordResetToken(); // Xem danh sách (chưa quan trọng)
   void addPasswordResetToken(PasswordResetToken pwdToken); // Thêm
//    void deletePasswordResetTokens(List<PasswordResetToken> listPwdToken); // Xóa (bớt lưu trữ) -> có thể không tốt
} 
// Để sau