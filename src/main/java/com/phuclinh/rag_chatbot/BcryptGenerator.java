package com.phuclinh.rag_chatbot;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptGenerator {
        public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "12"; // Mật khẩu gốc bạn muốn mã hóa
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("BCrypt: " + encodedPassword);
    }
}
