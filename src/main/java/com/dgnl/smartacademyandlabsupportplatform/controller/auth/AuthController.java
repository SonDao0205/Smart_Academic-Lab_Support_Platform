package com.dgnl.smartacademyandlabsupportplatform.controller.auth;

import com.dgnl.smartacademyandlabsupportplatform.exception.DataDuplicate;
import com.dgnl.smartacademyandlabsupportplatform.model.UserRoleEnum;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.AuthLoginDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.AuthRegisterDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.AuthService;
//import com.dgnl.smartacademyandlabsupportplatform.util.JwtTokenUtil;
import com.dgnl.smartacademyandlabsupportplatform.util.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthService authService,  JwtTokenUtil jwtTokenUtil) {
        this.authService = authService;
        this.jwtTokenUtil = jwtTokenUtil;
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
    public String login(@Valid @ModelAttribute("user") AuthLoginDTO authLoginDTO,
                        BindingResult bindingResult,
                        Model model,
                        HttpSession session,
                        HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        try {
            User u = authService.login(authLoginDTO);
            if (u == null) {
                model.addAttribute("error", "Đăng nhập thất bại!");
                return "auth/login";
            }
            // TỰ ĐỘNG TẠO TOKEN VÀ LƯU COOKIE
            String token = jwtTokenUtil.generateToken(u);
            Cookie cookie = new Cookie("remember-me", token);
            cookie.setMaxAge(86400000); // 10 ngày
            cookie.setPath("/");
            cookie.setHttpOnly(true); // Tránh bị JS tấn công lấy token
            response.addCookie(cookie);

            // Lưu Session
            session.setAttribute("user", u);
            session.setAttribute("role", u.getRole());
            String role =  u.getRole();
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
    public String logout(HttpSession session, HttpServletResponse response) {
        // 1. Xóa Session
        session.invalidate();

        // 2. Xóa Cookie
        Cookie cookie = new Cookie("remember-me", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/login";
    }
}
