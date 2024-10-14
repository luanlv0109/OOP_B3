package com.example.da.Controller;

import com.example.da.Service.LoginService;
import com.example.da.domain.User;
import com.example.da.utils.Constant;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")

public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // Trả về trang login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {

        User user = loginService.login(username, password);
        if (user != null && user.getPassword().equals(password)) {  // Kiểm tra username và password
            // Lưu trữ thông tin người dùng vào session
            session.setAttribute("user", user);

            if (user.isAdmin()) {
                return "redirect:/admin";  // Nếu là admin, điều hướng đến trang admin
            } else {
                return "redirect:/user";  // Nếu là user thường, điều hướng đến trang người dùng
            }
        } else {
            model.addAttribute("error", Constant.LOGIN_ERROR);
            return "login";  // Nếu đăng nhập không thành công, quay lại trang login
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Xóa session khi người dùng logout
        return "redirect:/login";  // Chuyển hướng về trang đăng nhập
    }

}
