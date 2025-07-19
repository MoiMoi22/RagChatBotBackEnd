package com.phuclinh.rag_chatbot.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phuclinh.rag_chatbot.dto.ChangePasswordRequestDTO;
import com.phuclinh.rag_chatbot.dto.CreateUserDTO;
import com.phuclinh.rag_chatbot.dto.UpdateUserRequestDTO;
import com.phuclinh.rag_chatbot.dto.UserDTO;
import com.phuclinh.rag_chatbot.entity.Department;
import com.phuclinh.rag_chatbot.entity.Role;
import com.phuclinh.rag_chatbot.entity.User;
import com.phuclinh.rag_chatbot.enums.UserStatus;
import com.phuclinh.rag_chatbot.exception.BadRequestException;
import com.phuclinh.rag_chatbot.exception.DuplicateResourceException;
import com.phuclinh.rag_chatbot.exception.InvalidPasswordException;
import com.phuclinh.rag_chatbot.exception.ResourceNotFoundException;
import com.phuclinh.rag_chatbot.repository.DepartmentRepository;
import com.phuclinh.rag_chatbot.repository.RoleRepository;
import com.phuclinh.rag_chatbot.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(CreateUserDTO userReq){
        if (userRepository.existsByUsername(userReq.getUserName())) {
            throw new DuplicateResourceException("Username đã được sử dụng");
        }

        if (userRepository.existsByEmail(userReq.getEmail())) {
            throw new DuplicateResourceException("Email đã được sử dụng");
        }

        Role role = roleRepository.findById(userReq.getRoleId())
        .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));

        Department department = departmentRepository.findById(userReq.getDepartmentId())
        .orElseThrow(() -> new ResourceNotFoundException("Department không tồn tại"));

        User user = new User();
        user.setUsername(userReq.getUserName());
        user.setEmail(userReq.getEmail());
        user.setPassword(passwordEncoder.encode(userReq.getPassword()));
        user.setFullName(userReq.getFullName());
        user.setRole(role);
        user.setDepartment(department);
        user.setLastChangeAt(user.getCreatedAt());
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }
    public UserDTO getUserForViewing(String requestedUsername, Authentication auth) {
        String currentUsername = auth.getName();
    // boolean isAdmin = auth.getAuthorities().stream()
    //         .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

    // if (isAdmin || currentUsername.equals(requestedUsername)) {
        if(currentUsername.equals(requestedUsername)){
            User user = userRepository.findByUsername(requestedUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
            return mapToDto(user);
        }
        else {
            throw new AccessDeniedException("Bạn không có quyền xem thông tin user này");
        }
    }

    private UserDTO mapToDto(User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getEmail(),
            user.getRole() != null ? user.getRole().getId() : null,
            user.getDepartment() != null ? user.getDepartment().getId() : null,
            user.getCreatedAt(),
            user.getStatus() != null ? user.getStatus() : null
        );
    }

    public void changePassword(String authenticatedUsername, ChangePasswordRequestDTO request, boolean isAdmin) {
        String targetUsername = request.getUserName() != null ? request.getUserName() : authenticatedUsername;

        if (!isAdmin && !targetUsername.equals(authenticatedUsername)) {
            throw new AccessDeniedException("Bạn không có quyền thay đổi mật khẩu của người khác");
        }

        User user = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        if (!isAdmin && !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Mật khẩu hiện tại không đúng");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setLastChangeAt(LocalDateTime.now());
        userRepository.save(user);
    }

        public String logout() {
        // Phát triển ghi log đăng xuất ở đây
        return "Đăng xuất thành công. Vui lòng xóa token ở phía client.";
    }

public void updateUserInfo(String authenticatedUsername, UpdateUserRequestDTO request, boolean isAdmin) {
    String targetUsername = (request.getUserName() != null && !request.getUserName().isBlank())
            ? request.getUserName()
            : authenticatedUsername;

    if (!isAdmin && !targetUsername.equals(authenticatedUsername)) {
        throw new AccessDeniedException("Bạn không có quyền cập nhật thông tin người dùng khác");
    }

    User user = userRepository.findByUsername(targetUsername)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

    if (request.getFullName() != null && !request.getFullName().isBlank()) {
        user.setFullName(request.getFullName());
    }

    if (request.getEmail() != null && !request.getEmail().isBlank() && !request.getEmail().equalsIgnoreCase(user.getEmail())) {
        userRepository.findByEmail(request.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(user.getId())) {
                throw new DuplicateResourceException("Email đã được sử dụng bởi người dùng khác");
            }
        });
        user.setEmail(request.getEmail());
    }
    if (isAdmin && request.getStatus() != null) {
        try {
            user.setStatus(UserStatus.valueOf(request.getStatus())); // dùng setter String
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Trạng thái không hợp lệ");
        }
    }
    userRepository.save(user);
}

public List<UserDTO> getUsers(String authenticatedUsername, Long departmentId, String status) {
    User currentUser = userRepository.findByUsername(authenticatedUsername)
    .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
    String userRole = currentUser.getRole().getName();

    System.out.println(userRole);

    if(userRole.equalsIgnoreCase("MANAGER") && departmentId != currentUser.getDepartment().getId()){
        throw new AccessDeniedException("Bạn Không có quyền xem danh sách phòng ban khác");
    }
    if(userRole.equalsIgnoreCase("EMPLOYEE")){
        throw new AccessDeniedException("Bạn Không có quyền xem danh sách phòng ban");
    }
    UserStatus convertedStatus = null;
    if(status != null && !status.isBlank()){
        try {
            convertedStatus = UserStatus.valueOf(status.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            throw new BadRequestException("Trạng thái " + status + " không hợp lệ");
        }
    }
    List<User> users = userRepository.findUsersWithFilter(departmentId, convertedStatus);
    return users.stream().map(this::mapToDto).collect(Collectors.toList());
}

}
