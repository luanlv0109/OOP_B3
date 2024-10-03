package com.example.da.Controller;

import com.example.da.Service.DepartmentService;
import com.example.da.domain.Department;
import com.example.da.domain.User;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin/departments")
public class DepartmentAdminController {

    @Autowired
    private DepartmentService departmentService;

     // Hiển thị danh sách phòng ban
    @GetMapping("/list")
    public String viewDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "department/list";  // Chuyển hướng đến trang list.html trong thư mục department
    }

    // Hiển thị form thêm phòng ban
    @GetMapping("/add")
    public String showAddForm(Model model , HttpSession session) {
        model.addAttribute("department", new Department());
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Redirect to login page if no user is found in session
            return "redirect:/login";
        }
        return "department/add";  // Chuyển hướng đến trang add.html trong thư mục department
    }

    // Xử lý thêm phòng ban
    @PostMapping("/add")
    public String addDepartment(@ModelAttribute("department") Department department,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        // Kiểm tra lỗi khi nhập liệu
        if (bindingResult.hasErrors()) {
            return "department/add";
        }

        if(!departmentService.saveDepartment(department)){
            log.info("vao day:{}" , departmentService.saveDepartment(department));
            model.addAttribute("errorMessage", "Thông tin phòng ban đã tồn tại");
            return "department/add";
        }

        departmentService.saveDepartment(department);
        redirectAttributes.addFlashAttribute("successMessage", "Phòng ban đã được thêm thành công");
        return "redirect:/admin/departments/list";  // Chuyển hướng lại danh sách phòng ban
    }

    // Hiển thị form chỉnh sửa phòng ban
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            return "redirect:/admin/departments/list";  // Nếu không tìm thấy phòng ban, chuyển hướng về danh sách
        }
        model.addAttribute("department", department);
        return "department/edit";  // Chuyển hướng đến trang edit.html trong thư mục department
    }

    // Xử lý cập nhật phòng ban
    @PostMapping("/edit/{id}")
    public String updateDepartment(@PathVariable Long id,
                                   @ModelAttribute("department") Department department,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        // Kiểm tra lỗi khi nhập liệu
        if (bindingResult.hasErrors()) {
            return "department/edit";
        }

        department.setId(id);  // Đảm bảo ID được đặt đúng

        if(!departmentService.saveDepartment(department)){
            model.addAttribute("errorMessage", "Thông tin phòng ban đã tồn tại");
            return "department/edit";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Phòng ban đã được cập nhật thành công");
        return "redirect:/admin/departments/list";  // Chuyển hướng lại danh sách phòng ban
    }

    // Xử lý xóa phòng ban
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        departmentService.deleteDepartment(id);
        redirectAttributes.addFlashAttribute("successMessage", "Phòng ban đã được xóa thành công");
        return "redirect:/admin/departments/list";  // Chuyển hướng lại danh sách phòng ban
    }
}