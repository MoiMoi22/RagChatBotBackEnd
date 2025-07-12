package com.phuclinh.rag_chatbot.repository;

import com.phuclinh.rag_chatbot.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {    
}
