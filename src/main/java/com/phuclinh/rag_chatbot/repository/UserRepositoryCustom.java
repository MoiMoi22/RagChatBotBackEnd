package com.phuclinh.rag_chatbot.repository;

import java.util.List;

import com.phuclinh.rag_chatbot.entity.User;
import com.phuclinh.rag_chatbot.enums.UserStatus;

public interface UserRepositoryCustom {
    List<User> findUsersWithFilter(Long departmentId, UserStatus status);
}
