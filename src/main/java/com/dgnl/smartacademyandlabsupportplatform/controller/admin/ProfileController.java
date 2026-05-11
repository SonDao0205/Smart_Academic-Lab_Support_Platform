package com.dgnl.smartacademyandlabsupportplatform.controller.admin;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("admin_profile_controller")
@RequestMapping("/admin")
public class ProfileController {
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User sessionUser =(User) session.getAttribute("user");

        model.addAttribute("user", sessionUser);
        return "admin/profile";
    }
}
