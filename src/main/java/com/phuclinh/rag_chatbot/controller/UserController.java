package com.phuclinh.rag_chatbot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phuclinh.rag_chatbot.dto.ApiResponseDTO;
import com.phuclinh.rag_chatbot.dto.CreateUserDTO;
import com.phuclinh.rag_chatbot.dto.UserDTO;
import com.phuclinh.rag_chatbot.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO> createUser(@Valid @RequestBody CreateUserDTO request) {
        userService.createUser(request);
        return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(new ApiResponseDTO(201, "Tạo tài khoản thành công"));
    }

    @GetMapping("/{username}")
    public UserDTO getUserByUsername(@PathVariable String username, Authentication authentication) {
        return userService.getUserForViewing(username, authentication);
    }
    
}
