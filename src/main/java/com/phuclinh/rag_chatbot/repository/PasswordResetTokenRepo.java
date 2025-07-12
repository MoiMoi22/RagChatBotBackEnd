package com.phuclinh.rag_chatbot.repository;

import com.phuclinh.rag_chatbot.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long>{
}
