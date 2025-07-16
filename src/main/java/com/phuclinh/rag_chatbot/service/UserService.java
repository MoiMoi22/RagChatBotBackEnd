package com.phuclinh.rag_chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phuclinh.rag_chatbot.dto.CreateUserDTO;
import com.phuclinh.rag_chatbot.dto.UserDTO;
import com.phuclinh.rag_chatbot.entity.Department;
import com.phuclinh.rag_chatbot.entity.Role;
import com.phuclinh.rag_chatbot.entity.User;
import com.phuclinh.rag_chatbot.exception.DuplicateResourceException;
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
        user.setStatus(1);

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
        user.getCreatedAt()
    );
}
}
