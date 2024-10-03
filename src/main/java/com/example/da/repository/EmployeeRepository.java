package com.example.da.repository;

import com.example.da.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Kiểm tra trùng lặp khi tạo mới
    boolean existsByCode(String code);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    // Kiểm tra trùng lặp khi cập nhật (bỏ qua nhân viên với cùng id)
    boolean existsByCodeAndIdNot(String code, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByPhoneAndIdNot(String phone, Long id);

}