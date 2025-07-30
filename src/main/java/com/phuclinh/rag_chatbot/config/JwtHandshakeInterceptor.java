package com.phuclinh.rag_chatbot.config;

import com.phuclinh.rag_chatbot.service.CustomUserDetailsService;
import com.phuclinh.rag_chatbot.util.JwtUtil;
import com.phuclinh.rag_chatbot.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        String query = request.getURI().getQuery();
        if (query != null && query.contains("token=")) {
            String raw = query.split("token=")[1];
            String token = URLDecoder.decode(raw, StandardCharsets.UTF_8);

            if (token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                String username = jwtUtil.extractUsername(jwt);
                CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.isTokenValid(jwt, username, userDetails)) {
                    attributes.put("user", userDetails); // Giao cho HandshakeHandler
                    System.out.println("[HandshakeInterceptor] Gán user: " + username);
                } else {
                    System.out.println("Token không hợp lệ");
                }
            }
        }

        return true;
    }
    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // No-op
    }

    private String extractToken(ServerHttpRequest request) {
        // Ưu tiên header, fallback query param
        var headers = request.getHeaders();
        if (headers.containsKey("Authorization")) {
            return headers.getFirst("Authorization");
        }
        var query = request.getURI().getQuery();
        if (query != null && query.contains("token=")) {
            return "Bearer " + query.split("token=")[1];
        }
        return null;
    }
}
