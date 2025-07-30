package com.phuclinh.rag_chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws") // base path
public class WebSocketUserController {

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @GetMapping("/users")
    public void printUsers() {
        System.out.println("Hello");
        simpUserRegistry.getUsers().forEach(user -> {
            System.out.println("Connected user: " + user.getName());
            user.getSessions().forEach(session ->
                System.out.println("session: " + session.getId())
            );
        });
    }
}

