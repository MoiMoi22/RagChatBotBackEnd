package com.phuclinh.rag_chatbot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phuclinh.rag_chatbot.dto.ApiResponseDTO;
import com.phuclinh.rag_chatbot.dto.ChangePasswordRequestDTO;
import com.phuclinh.rag_chatbot.dto.CreateUserDTO;
import com.phuclinh.rag_chatbot.dto.UpdateUserRequestDTO;
import com.phuclinh.rag_chatbot.dto.UserDTO;
import com.phuclinh.rag_chatbot.exception.BadRequestException;
import com.phuclinh.rag_chatbot.service.UserService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


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

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponseDTO> changePassword(@RequestBody @Valid ChangePasswordRequestDTO request,
                                            Authentication authentication) {
        String currentUser = authentication.getName();
        // boolean isAdmin = authentication.getAuthorities().stream()
        //     .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        String targetUser = request.getUserName() != null && !request.getUserName().isBlank() ? request.getUserName() : currentUser;

        // Nếu đang đổi mật khẩu của chính mình, bất kể role, thì phải nhập currentPassword
        boolean isChangingOwnPassword = targetUser.equals(currentUser);
        boolean missingCurrentPassword = request.getCurrentPassword() == null || request.getCurrentPassword().isBlank();

        if (isChangingOwnPassword && missingCurrentPassword) {
            throw new BadRequestException("Vui lòng nhập mật khẩu hiện tại");
        }

        userService.changePassword(currentUser, request, false);
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(new ApiResponseDTO(200, "Thay đổi mật khẩu thành công"));
    }

    @PutMapping("/update-info")
    public ResponseEntity<ApiResponseDTO> updateUserInfo(@RequestBody @Valid UpdateUserRequestDTO request, Authentication authentication) {

        String authenticatedUsername = authentication.getName();
        // boolean isAdmin = authentication.getAuthorities().stream()
        //     .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        userService.updateUserInfo(authenticatedUsername, request, false);

        return ResponseEntity.ok(new ApiResponseDTO(200, "Cập nhật thông tin thành công"));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String status,
            Authentication authentication
    ) {
        String authenticatedUsername = authentication.getName();
        List<UserDTO> users = userService.getUsers(authenticatedUsername, departmentId, status);
        return ResponseEntity.ok(users);
    }
}
