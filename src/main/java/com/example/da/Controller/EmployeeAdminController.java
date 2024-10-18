package com.example.da.Controller;

import com.example.da.Service.DepartmentService;
import com.example.da.Service.EmployeeService;
import com.example.da.Service.FilesStorageService;
import com.example.da.domain.Employee;
import com.example.da.domain.User;
import com.example.da.dto.EmployeeDTO;
import com.example.da.utils.Constant;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.checkerframework.checker.units.qual.A;
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
import java.nio.file.Files;

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
    public String viewEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employee/list";
    }
    @GetMapping("/add")
    public String showAddForm(Model model) {
        // Tạo một đối tượng EmployeeDTO rỗng để liên kết với form
        model.addAttribute("employee", new EmployeeDTO());

        // Lấy danh sách các phòng ban để hiển thị trong form
        model.addAttribute("departments", departmentService.getAllDepartments());

        // Trả về view "employee/add" để hiển thị form thêm nhân viên
        return "employee/add";
    }
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("employee") @Valid EmployeeDTO employeeDTO,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employee/add"; // Nếu có lỗi, trả về form thêm nhân viên
        }

        try {
            if (!employeeService.saveEmployee(employeeDTO , imageFile)) {
                redirectAttributes.addFlashAttribute("errorMessage", Constant.EMPLOYEE_EXIST_ERROR);
                model.addAttribute("departments", departmentService.getAllDepartments());
                return "employee/add"; // Nếu phát hiện trùng lặp, trả về trang thêm nhân viên
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employee/add"; // Nếu gặp lỗi, quay lại trang thêm nhân viên
        }

        redirectAttributes.addFlashAttribute("successMessage", Constant.EMPLOYEE_ADD_SUCCESS);
        return "redirect:/admin/employees/list";
    }

    // Hiển thị form sửa nhân viên
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);

        if (employeeDTO == null) {
            return "redirect:/admin/employees/list";
        }

        model.addAttribute("employee", employeeDTO);
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "employee/edit";
    }

    // Xử lý cập nhật nhân viên
    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute("employee") @Valid EmployeeDTO employeeDTO,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", employeeDTO);
            model.addAttribute("departments", departmentService.getAllDepartments());
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "employee/edit";
        }

        try {
            if (!employeeService.updateEmployee(id, employeeDTO , imageFile)) {
                redirectAttributes.addFlashAttribute("errorMessage", Constant.EMPLOYEE_EXIST_ERROR);
                model.addAttribute("departments", departmentService.getAllDepartments());
                return "employee/edit"; // Nếu phát hiện trùng lặp, trả về trang chỉnh sửa nhân viên
            }
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employee/edit"; // Quay lại trang chỉnh sửa với thông báo lỗi
        }

        redirectAttributes.addFlashAttribute("successMessage", Constant.EMPLOYEE_UPDATE_SUCCESS);
        return "redirect:/admin/employees/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("successMessage", Constant.EMPLOYEE_DELETE_SUCCESS);
        return "redirect:/admin/employees/list";
    }

    @GetMapping("/files/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Resource file = filesStorageService.load(filename);

            // Lấy loại nội dung của tệp
            String contentType = Files.probeContentType(file.getFile().toPath());

            // Nếu không xác định được loại nội dung, mặc định là "application/octet-stream"
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"") // Thay "attachment" bằng "inline"
                    .body(file);
        } catch (IOException e) {
            log.error("Error loading file: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }



    @GetMapping("/detail/{id}")
    public String viewEmployeeDetails(@PathVariable Long id, Model model) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
        model.addAttribute("employeeDTO", employeeDTO);
        return "employee/detail";
    }

}
