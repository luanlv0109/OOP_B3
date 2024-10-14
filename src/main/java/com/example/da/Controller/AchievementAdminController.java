package com.example.da.Controller;

import com.example.da.Service.AchievementService;
import com.example.da.Service.EmployeeService;
import com.example.da.domain.User;
import com.example.da.dto.AchievementDTO;
import com.example.da.dto.EmployeeSummaryDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.example.da.utils.Constant.*;

@Controller
@RequestMapping("/admin/achievements")
public class AchievementAdminController {

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/list")
    public String viewAchievements(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // Redirect to login page if no user is found in session
        }
        model.addAttribute("achievements", achievementService.getAllAchievements());
        return "achievement/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("achievement", new AchievementDTO());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "achievement/add";
    }

    @PostMapping("/add")
    public String addAchievement(@Valid @ModelAttribute("achievement") AchievementDTO achievementDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "achievement/add"; // Return to the add form if there are validation errors
        }

        try {
            if (!achievementService.addAchievement(achievementDTO)) {
                redirectAttributes.addFlashAttribute("errorMessage", ACHIEVEMENT_EXIST_ERROR);
                return "redirect:/admin/achievements/add"; // Redirect back to add form if achievement already exists
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/achievements/add"; // Redirect back to add form if an exception occurs
        }

        redirectAttributes.addFlashAttribute("successMessage", ACHIEVEMENT_ADD_SUCCESS);
        return "redirect:/admin/achievements/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        AchievementDTO achievementDTO;
        try {
            achievementDTO = achievementService.getAchievementById(id);
            if (achievementDTO == null) {
                throw new RuntimeException(ACHIEVEMENT_NOT_FOUND_ERROR);
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/achievements/list"; // Redirect to list if achievement not found
        }

        model.addAttribute("achievement", achievementDTO);
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "achievement/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateAchievement(@Valid @PathVariable Long id,
                                    @ModelAttribute("achievement") AchievementDTO achievementDto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "achievement/edit"; // Return to the edit form if there are validation errors
        }

        try {
            achievementDto.setId(id);
            if (!achievementService.updateAchievement(achievementDto)) {
                redirectAttributes.addFlashAttribute("errorMessage", ACHIEVEMENT_EXIST_ERROR);
                return "redirect:/admin/achievements/edit/" + id; // Redirect back to edit form if achievement already exists
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "achievement/edit"; // Return to edit form if an exception occurs
        }

        redirectAttributes.addFlashAttribute("successMessage", ACHIEVEMENT_UPDATE_SUCCESS);
        return "redirect:/admin/achievements/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteAchievement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        achievementService.deleteAchievement(id);
        redirectAttributes.addFlashAttribute("successMessage", ACHIEVEMENT_DELETE_SUCCESS);
        return "redirect:/admin/achievements/list";
    }

    @GetMapping("/summary")
    public String showEmployeeAchievements(Model model) {
        List<EmployeeSummaryDTO> employeeSummaries = achievementService.getEmployeeAchievementSummary();
        model.addAttribute("employeeSummaries", employeeSummaries);
        return "summary/employeeAchievementSummary";
    }
}
