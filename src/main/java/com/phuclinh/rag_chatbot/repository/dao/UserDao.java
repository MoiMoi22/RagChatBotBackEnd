package com.phuclinh.rag_chatbot.repository.dao;

import java.util.List;

import com.phuclinh.rag_chatbot.entity.User;

public interface UserDao {
    List<User> findAllUser();
    User getUser(String username, String password); // Đăng nhập
    User getUser(String mail); // Quên mật khẩu
    void addUser(User user); // Thêm nhân vên
    void updateUser(User user); // Cập nhập nhân viên(Đổi phòng ban phát triển sau)/Phân quyền/Thay đổi mật khẩu
    void deleteUser(Long id); // Xóa nhân viên (Phát triển sau)
}

// 