package com.phuclinh.rag_chatbot.controller;

import com.phuclinh.rag_chatbot.dto.ApiResponseDTO;
import com.phuclinh.rag_chatbot.dto.LoginRequestDTO;
import com.phuclinh.rag_chatbot.dto.ResetPasswordRequestDTO;
import com.phuclinh.rag_chatbot.service.PasswordResetService;
import com.phuclinh.rag_chatbot.service.UserService;
import com.phuclinh.rag_chatbot.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordResetService passwordResetService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public String generateToken(@RequestBody LoginRequestDTO loginReq) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword())
        );
        return jwtUtil.generateToken(loginReq.getUsername());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO> logout() {
        String message = userService.logout();
        return ResponseEntity.ok(new ApiResponseDTO(HttpStatus.OK.value(), message));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDTO> forgotPassword(@RequestParam String email) {
        passwordResetService.createPasswordResetToken(email);
        return ResponseEntity.ok(new ApiResponseDTO(HttpStatus.OK.value(), "Hướng dẫn đặt lại mật khẩu đã được gửi đến email."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDTO> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(new ApiResponseDTO(HttpStatus.OK.value(), "Mật khẩu đã được thay đổi."));
    }

}