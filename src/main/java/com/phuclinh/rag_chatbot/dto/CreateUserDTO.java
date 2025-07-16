package com.phuclinh.rag_chatbot.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateUserDTO {

    @NotBlank(message = "Username không được để trống")
    private String userName;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải dài ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotNull(message = "Role không được để trống")
    private Long roleId;

    @NotNull(message = "Department không được để trống")
    private Long departmentId;
    
}