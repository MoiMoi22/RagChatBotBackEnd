package com.phuclinh.rag_chatbot.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phuclinh.rag_chatbot.entity.PasswordResetToken;
import com.phuclinh.rag_chatbot.entity.User;
import com.phuclinh.rag_chatbot.exception.InvalidTokenException;
import com.phuclinh.rag_chatbot.exception.ResourceNotFoundException;
import com.phuclinh.rag_chatbot.repository.PasswordResetTokenRepository;
import com.phuclinh.rag_chatbot.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        tokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setExpiration(LocalDateTime.now().plusMinutes(30));
        resetToken.setUser(user);
        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        String message = "<p>Xin chào,</p>" +
                         "<p>Click vào link bên dưới để đặt lại mật khẩu:</p>" +
                         "<a href=\"" + resetLink + "\">Đặt lại mật khẩu</a>" +
                         "<p>Liên kết này sẽ hết hạn sau 30 phút.</p>";

        emailService.sendEmail(user.getEmail(), "Đặt lại mật khẩu", message);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token không hợp lệ"));

        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token đã hết hạn");
        }

        User user = resetToken.getUser();
        if (user == null || !userRepository.existsById(user.getId())) {
            throw new InvalidTokenException("Người dùng không còn tồn tại");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastChangeAt(LocalDateTime.now());
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }
}
