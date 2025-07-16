package com.phuclinh.rag_chatbot.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.phuclinh.rag_chatbot.dto.LoginRequest;
import com.phuclinh.rag_chatbot.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtil;
    @PostMapping("/authenticate")    
    public String generateToken(@RequestBody LoginRequest loginReq ){
                    System.out.println(1);
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
    @GetMapping("/test")
    public String test() {
        return "Hello";
    }
    
}
