package com.phuclinh.rag_chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.phuclinh.rag_chatbot.entity.User;
import com.phuclinh.rag_chatbot.exception.InvalidTokenException;
import com.phuclinh.rag_chatbot.repository.UserRepository;
import com.phuclinh.rag_chatbot.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameWithRole(username)
                .orElseThrow(() -> new InvalidTokenException("Không tìm thấy username"));
        return new CustomUserDetails(user);
    }
}
