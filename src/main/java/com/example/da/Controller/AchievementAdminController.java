package com.example.da.Controller;


import com.example.da.Service.AchievementService;
import com.example.da.Service.EmployeeService;
import com.example.da.domain.Achievement;
import com.example.da.domain.User;
import com.example.da.dto.EmployeeSummaryDTO;
import jakarta.persistence.EntityResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/achievements")

public class AchievementAdminController {

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private EmployeeService employeeService;

    // Hiển thị danh sách thành tích
    @GetMapping("/list")
    public String viewAchievements(Model model , HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Redirect to login page if no user is found in session
            return "redirect:/login";
        }
        model.addAttribute("achievements", achievementService.getAllAchievements());
        return "achievement/list";
    }

    // Hiển thị form thêm thành tích
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("achievement", new Achievement());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "achievement/add";
    }

    // Xử lý thêm thành tích
    @PostMapping("/add")
    public String addAchievement(@ModelAttribute("achievement") Achievement achievement,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "achievement/add";
        }

        // Check for duplicates
        if (!achievementService.saveAchievement(achievement)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thành tích đã tồn tại");
            return "redirect:/admin/achievements/add";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Thành tích đã được thêm thành công");
        return "redirect:/admin/achievements/list";
    }

    // Hiển thị form chỉnh sửa thành tích
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Achievement achievement = achievementService.getAchievementById(id);
        if (achievement == null) {
            return "redirect:/admin/achievements/list";  // Nếu không tìm thấy thành tích, quay lại danh sách
        }
        model.addAttribute("achievement", achievement);
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "achievement/edit";
    }

        // Xử lý cập nhật thành tích
        @PostMapping("/edit/{id}")
        public String updateAchievement(@PathVariable Long id,
                                        @ModelAttribute("achievement") Achievement achievement,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {
            if (bindingResult.hasErrors()) {
                model.addAttribute("employees", employeeService.getAllEmployees());
                return "achievement/edit";
            }

            achievement.setId(id);

            // Check for duplicates during the update
            if (!achievementService.saveAchievement(achievement)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Thành tích đã tồn tại");
                return "redirect:/admin/achievements/edit/" + id;
            }

            redirectAttributes.addFlashAttribute("successMessage", "Thành tích đã được cập nhật thành công");
            return "redirect:/admin/achievements/list";
        }

    // Xử lý xóa thành tích
    @GetMapping("/delete/{id}")
    public String deleteAchievement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        achievementService.deleteAchievement(id);
        redirectAttributes.addFlashAttribute("successMessage", "Thành tích đã được xóa thành công");
        return "redirect:/admin/achievements/list";
    }

    // Hiển thị tổng hợp thành tích nhân viên
    @GetMapping("/summary")
    public String showEmployeeAchievements(Model model) {
        List<EmployeeSummaryDTO> employeeSummaries = achievementService.getEmployeeAchievementSummary();
        model.addAttribute("employeeSummaries", employeeSummaries);
        return "summary/employeeAchievementSummary";
    }

}
