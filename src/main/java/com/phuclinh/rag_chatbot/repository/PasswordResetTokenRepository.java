package com.phuclinh.rag_chatbot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phuclinh.rag_chatbot.entity.PasswordResetToken;
import com.phuclinh.rag_chatbot.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);

} 