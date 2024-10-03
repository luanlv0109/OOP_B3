package com.example.da.Controller;

import com.example.da.Service.DepartmentService;
import com.example.da.Service.EmployeeService;
import com.example.da.Service.FilesStorageService;
import com.example.da.domain.Employee;
import com.example.da.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;

@Slf4j
@Controller
@RequestMapping("/admin/employees")

public class EmployeeAdminController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private FilesStorageService filesStorageService;

    // Hiển thị danh sách nhân viên
    @GetMapping("/list")
    public String viewEmployees(Model model , HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Redirect to login page if no user is found in session
            return "redirect:/login";
        }
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employee/list";  // Chuyển hướng đến file list.html trong thư mục employee
    }

    // Hiển thị form thêm nhân viên
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "employee/add";  // Chuyển hướng đến file add.html trong thư mục employee
    }

    // Xử lý thêm nhân viên
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("employee") Employee employee,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) throws IOException {

            String path = filesStorageService.save(imageFile);
            employee.setImage(path);
        // Nếu có lỗi, trả về form thêm nhân viên và hiện thông báo lỗi
        if (bindingResult.hasErrors()) {
            return "employee/add";
        }

        if (!employeeService.saveEmployee(employee)) {
            model.addAttribute("errorMessage", "Thông tin mã, email hoặc số điện thoại đã tồn tại");
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employee/add";  // Nếu phát hiện trùng lặp, trả về trang thêm nhân viên
        }

        redirectAttributes.addFlashAttribute("successMessage", "Nhân viên đã được thêm thành công");
        return "redirect:/admin/employees/list"; // Chuyển hướng lại danh sách nhân viên
    }

    // Hiển thị form sửa nhân viên
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);

        if (employee == null) {
            return "redirect:/admin/employees/list";  // Nếu không tìm thấy nhân viên, chuyển hướng về danh sách
        }

        log.info("Employee details: {}", employee); // Log chi tiết về employee

        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "employee/edit";  // Chuyển hướng đến file edit.html trong thư mục employee
    }

    // Xử lý cập nhật nhân viên
    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute("employee") Employee employee,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) throws IOException {


        if (bindingResult.hasErrors()) {
            return "employee/edit";
        }
        employee.setId(id);

        if (!employeeService.updateEmployee(employee , imageFile)) {
            model.addAttribute("errorMessage", "Thông tin mã, email hoặc số điện thoại đã tồn tại");
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employee/edit";  // Nếu phát hiện trùng lặp, trả về trang sửa nhân viên
        }

        redirectAttributes.addFlashAttribute("successMessage", "Nhân viên đã được cập nhật thành công");
        return "redirect:/admin/employees/list";  // Chuyển hướng lại danh sách nhân viên
    }

    // Xử lý xóa nhân viên
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("successMessage", "Nhân viên đã được xóa thành công");
        return "redirect:/admin/employees/list";  // Chuyển hướng lại danh sách nhân viên
    }

    // Xem chi tiết thông tin nhân viên
    @GetMapping("/detail/{id}")
    public String viewEmployeeDetails(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        log.info("employee img: {}", employee.getImage());
        model.addAttribute("employee", employee);
        return "employee/detail";
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
        log.info("vao dat ");
        Resource file = filesStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +"anh" + "\"")
                .body(file);
    }


}
