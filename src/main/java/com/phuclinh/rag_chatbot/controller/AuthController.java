package com.phuclinh.rag_chatbot.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phuclinh.rag_chatbot.dto.ApiResponseDTO;
import com.phuclinh.rag_chatbot.dto.LoginRequestDTO;
import com.phuclinh.rag_chatbot.service.UserService;
import com.phuclinh.rag_chatbot.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @PostMapping("/login")    
    public String generateToken(@RequestBody LoginRequestDTO loginReq ){
        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword())
            );
            String token = jwtUtil.generateToken(loginReq.getUsername());
            return token;
        }
        catch(Exception e){
            throw e;
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        String message = userService.logout();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO(200, message));
    }

    @GetMapping("/test")
    public String test() {
        return "Hello";
    }
    
}
