package com.example.da.Controller;

import com.example.da.Service.DepartmentService;
import com.example.da.domain.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // Hiển thị danh sách phòng ban
    @GetMapping
    public String viewDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "department/list";  // Chuyển hướng đến file list.html trong thư mục department
    }

    // Hiển thị form thêm phòng ban
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        return "department/add";  // Chuyển hướng đến file add.html trong thư mục department
    }

    // Xử lý thêm phòng ban
    @PostMapping("/add")
    public String addDepartment(@ModelAttribute Department department) {
        departmentService.saveDepartment(department);
        return "redirect:/admin/departments";  // Chuyển hướng lại danh sách phòng ban
    }

    // Hiển thị form sửa phòng ban
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        model.addAttribute("department", department);
        return "department/edit";  // Chuyển hướng đến file edit.html trong thư mục department
    }

    // Xử lý cập nhật phòng ban
    @PostMapping("/edit/{id}")
    public String updateDepartment(@PathVariable Long id, @ModelAttribute Department department) {
        department.setId(id);
        departmentService.saveDepartment(department);
        return "redirect:/admin/departments";  // Chuyển hướng lại danh sách phòng ban
    }

    // Xử lý xóa phòng ban
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/admin/departments";  // Chuyển hướng lại danh sách phòng ban
    }
}