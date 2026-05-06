package com.dgnl.smartacademyandlabsupportplatform.controller.auth;

import com.dgnl.smartacademyandlabsupportplatform.exception.DataDuplicate;
import com.dgnl.smartacademyandlabsupportplatform.model.UserRoleEnum;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.AuthLoginDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.AuthRegisterDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new AuthLoginDTO());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") AuthLoginDTO authLoginDTO, BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        try {
            User u = authService.login(authLoginDTO);
            if (u == null) {
                model.addAttribute("error", "Đăng nhập thất bại!");
                return "auth/login";
            }
            session.setAttribute("user", u);
            String role = u.getRole();
            session.setAttribute("role", role);
            model.addAttribute("success","Đăng nhập thành công!");
            if (role.equals(UserRoleEnum.admin.toString())) {
                return "redirect:/admin";
            } else if (role.equals(UserRoleEnum.lecturer.toString())) {
                return "redirect:/lecturer";
            } else {
                return "redirect:/student";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi đăng nhập!");
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new AuthRegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") AuthRegisterDTO authRegisterDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            User u = authService.register(authRegisterDTO);
            if (u == null) {
                model.addAttribute("error", "Đăng ký thất bại!");
                return "auth/register";
            }
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công!");
            return "redirect:/login";
        } catch (DataDuplicate e) {
            bindingResult.rejectValue(e.getField(), e.getField() + "duplicate", e.getMessage());
            return "auth/register";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi đăng nhập!");
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logoutPage(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("role");
        return "redirect:/login";
    }
}
