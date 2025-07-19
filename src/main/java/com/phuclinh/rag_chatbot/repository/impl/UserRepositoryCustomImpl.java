package com.phuclinh.rag_chatbot.repository.impl;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import org.springframework.stereotype.Repository;

import com.phuclinh.rag_chatbot.entity.User;
import com.phuclinh.rag_chatbot.enums.UserStatus;
import com.phuclinh.rag_chatbot.repository.UserRepositoryCustom;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findUsersWithFilter(Long departmentId, UserStatus status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (departmentId != null) {
            predicates.add(cb.equal(user.get("department").get("id"), departmentId));
        }

        if (status != null) {
            predicates.add(cb.equal(user.get("status"), status));
        }

        query.select(user).where(cb.and(predicates.toArray(new Predicate[0])));
        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}