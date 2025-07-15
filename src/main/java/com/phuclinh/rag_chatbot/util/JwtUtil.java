package com.phuclinh.rag_chatbot.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
            .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60)) // 1 ngày
            .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
            .compact();
    }

//     public String extractUsername(String token) {
//         return Jwts.parserBuilder()
//             .setSigningKey(SECRET_KEY.getBytes())
//             .build()
//             .parseClaimsJws(token)
//             .getBody()
//             .getSubject();
//     }

//     public boolean isTokenValid(String token, UserDetails userDetails) {
//         return extractUsername(token).equals(userDetails.getUsername()) &&
//                !isTokenExpired(token);
//     }

//     private boolean isTokenExpired(String token) {
//         return Jwts.parserBuilder()
//             .setSigningKey(SECRET_KEY.getBytes())
//             .build()
//             .parseClaimsJws(token)
//             .getBody()
//             .getExpiration()
//             .before(new Date());
//     }
}
