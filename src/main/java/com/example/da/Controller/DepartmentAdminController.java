package com.example.da.Controller;

import com.example.da.Service.DepartmentService;
import com.example.da.dto.DepartmentDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.example.da.utils.Constant.*;

@Slf4j
@Controller
@RequestMapping("/admin/departments")
public class DepartmentAdminController {

    @Autowired
    private DepartmentService departmentService;

    // Display department list
    @GetMapping("/list")
    public String viewDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "department/list";
    }

    // Display add form
    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        model.addAttribute("departmentDTO", new DepartmentDTO());
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "department/add";
    }
    // Xử lý thêm phòng ban
    @PostMapping("/add")
    public String addDepartment(@ModelAttribute("departmentDTO") @Valid DepartmentDTO departmentDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "department/add";
        }

        if (!departmentService.addDepartment(departmentDTO)) {
            model.addAttribute("errorMessage", DEPARTMENT_EXIST_ERROR);
            return "department/add";
        }

        redirectAttributes.addFlashAttribute("successMessage", DEPARTMENT_ADD_SUCCESS);
        return "redirect:/admin/departments/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(id);
        if (departmentDTO == null) {
            return "redirect:/admin/departments/list";
        }
        model.addAttribute("departmentDTO", departmentDTO);
        return "department/edit";
    }

    // Xử lý cập nhật phòng ban
    @PostMapping("/edit/{id}")
    public String updateDepartment(@PathVariable Long id,
                                   @ModelAttribute("departmentDTO") @Valid DepartmentDTO departmentDTO,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "department/edit";
        }

        if (!departmentService.updateDepartment(id, departmentDTO)) {
            model.addAttribute("errorMessage", DEPARTMENT_EXIST_ERROR);
            return "department/edit";
        }

        redirectAttributes.addFlashAttribute("successMessage", DEPARTMENT_UPDATE_SUCCESS);
        return "redirect:/admin/departments/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        departmentService.deleteDepartment(id);
        redirectAttributes.addFlashAttribute("successMessage", DEPARTMENT_DELETE_SUCCESS);
        return "redirect:/admin/departments/list";
    }
}
