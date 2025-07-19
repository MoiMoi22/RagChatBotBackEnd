package com.phuclinh.rag_chatbot.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.phuclinh.rag_chatbot.enums.UserStatus;

@Entity
@Table(name = "[user]") // escape từ khóa
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String password;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_password_change_at")
    private LocalDateTime lastChangeAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status; // dùng trực tiếp enum kiểu STRING

}
