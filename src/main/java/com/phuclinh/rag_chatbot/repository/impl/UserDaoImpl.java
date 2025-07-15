package com.phuclinh.rag_chatbot.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.phuclinh.rag_chatbot.entity.User;
import com.phuclinh.rag_chatbot.repository.dao.UserDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAllUser() {
        String jpql = "SELECT u FROM User u";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        return query.getResultList();
    }

    @Override
    public Optional<User> getUser(String username) {
        try {
            String jpql = "SELECT u FROM User u WHERE u.username = :username";
            User user = entityManager.createQuery(jpql, User.class)
                                     .setParameter("username", username)
                                     .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty(); // Không tìm thấy user
        }
    }

        @Override
    public Boolean checkMail(String mail) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("email", mail)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }
    
}
