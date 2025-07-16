package com.phuclinh.rag_chatbot.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.phuclinh.rag_chatbot.security.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET;
    private SecretKey SECRET_KEY;
    @Value("${jwt.EXPIRATION_TIME}")
    private long EXPIRATION_TIME;

    @PostConstruct
    public void init() {
        this.SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 1 ngày
            .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims extractClaims(String token){
            return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return  extractClaims(token).getExpiration().before(new Date());
    }

        public boolean isTokenValid(String token, String username, CustomUserDetails userDetails) {
        return username.equals(userDetails.getUsername()) &&
               !isTokenExpired(token);
    }
}
