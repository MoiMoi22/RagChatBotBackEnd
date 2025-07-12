package com.phuclinh.rag_chatbot.repository.dao;

import java.util.List;

import com.phuclinh.rag_chatbot.entity.Department;

public interface DepartmentDao {
    List<Department> findAllDepartments(); // Liệt kê tất cả department
    Department getDepartment(String name); // Lấy thông tin 1 department (có thể chưa cần thiết)
    void addDepartment(Department depart); // Thêm department
    void updateDepartment(Department depart); // Chưa cần thiết
    void deleteDepartment(String name); // Chưa cần thiết
}

// ==> Nằm trong thành phần cơ bản
