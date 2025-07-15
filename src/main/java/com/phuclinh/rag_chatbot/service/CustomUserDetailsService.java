package com.phuclinh.rag_chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.phuclinh.rag_chatbot.entity.User;
import com.phuclinh.rag_chatbot.repository.impl.UserDaoImpl;
import com.phuclinh.rag_chatbot.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDaoImpl userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new CustomUserDetails(user);
    }
}
