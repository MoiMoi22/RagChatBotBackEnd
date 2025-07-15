package com.phuclinh.rag_chatbot.repository.dao;

import java.util.List;
import java.util.Optional;

import com.phuclinh.rag_chatbot.entity.User;

public interface UserDao {
    List<User> findAllUser();
    Optional<User> getUser(String username); // Đăng nhập
    Boolean checkMail(String mail); // Quên mật khẩu
    void addUser(User user); // Thêm nhân vên
    void updateUser(User user); // Cập nhập nhân viên(Đổi phòng ban phát triển sau)/Phân quyền/Thay đổi mật khẩu
    void deleteUser(Long id); // Xóa nhân viên (Phát triển sau)
}

// 