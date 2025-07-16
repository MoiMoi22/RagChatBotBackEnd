package com.phuclinh.rag_chatbot.repository.impl;

import org.springframework.stereotype.Repository;

import com.phuclinh.rag_chatbot.repository.UserRepositoryCustom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

}
