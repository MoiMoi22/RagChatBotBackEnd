package com.phuclinh.rag_chatbot.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.phuclinh.rag_chatbot.exception.InvalidTokenException;
import com.phuclinh.rag_chatbot.security.CustomUserDetails;
import com.phuclinh.rag_chatbot.service.CustomUserDetailsService;
import com.phuclinh.rag_chatbot.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtil jwtUtil;
    
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    
    @Override
   protected void doFilterInternal(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull FilterChain filterChain)
                                throws ServletException, IOException {
            
        String header = request.getHeader("Authorization");
        String token=null;
        String username=null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.isTokenValid(token, username, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                throw new InvalidTokenException("Token không hợp lệ hoặc đã hết hạn");
            }
        }
        filterChain.doFilter(request, response);
    }
}
