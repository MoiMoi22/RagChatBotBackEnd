package com.phuclinh.rag_chatbot.repository.dao;

import java.util.List;

import com.phuclinh.rag_chatbot.entity.Role;

// Chưa quá quan trọng
public interface RoleDao {
    List<Role> findAllRole(); // Liệt kê tất cả các role
    Role addRole(Role role); // Thêm role
    void updateRole(Role role); // Cập nhật role
    void deleteRole(Long id);   // Xóa role
} 

// Hiện tại chỉ cần mỗi liệt kê và (add)
