package com.phuclinh.rag_chatbot.repository;

import com.phuclinh.rag_chatbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
