package com.example.da.Controller;

import com.example.da.Service.SearchKeywordService;
import com.example.da.dto.SearchKeywordDTO;
import com.example.da.utils.Constant;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin/keywords")
public class SearchKeywordController {

    @Autowired
    private SearchKeywordService searchKeywordService;


    @GetMapping("/add")
    public String showAddKeywordForm(Model model) {
        model.addAttribute("keyword", new SearchKeywordDTO());  // Đối tượng DTO cho form
        return "keyword/add";  // Trả về template keyword/add.html
    }

    // Xử lý thêm từ khóa mới
    @PostMapping("/add")
    public String addKeyword(@ModelAttribute("keyword") @Valid SearchKeywordDTO searchKeywordDTO,
                             BindingResult bindingResult,
                             Model model) {

        // Kiểm tra lỗi xác thực
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            model.addAttribute("errorMessage", errorMessage);  // Trả về trang với thông báo lỗi
            return "keyword/add";
        }

        // Xử lý thêm từ khóa
        try {
            if (!searchKeywordService.addKeyword(searchKeywordDTO)) {
                model.addAttribute("errorMessage", Constant.KEYWORD_EXIST_ERROR);
                return "keyword/add";  // Nếu từ khóa trùng lặp, quay lại trang thêm từ khóa
            }
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "keyword/add";  // Nếu có lỗi khác, quay lại trang thêm từ khóa
        }

        model.addAttribute("successMessage", Constant.KEYWORD_ADD_SUCCESS);  // Thêm thông báo thành công
        model.addAttribute("keyword", new SearchKeywordDTO());  // Reset form với DTO mới
        return "keyword/add";  // Trả về cùng trang thêm từ khóa
    }
}
