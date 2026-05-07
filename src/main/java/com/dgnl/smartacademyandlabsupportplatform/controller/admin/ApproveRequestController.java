package com.dgnl.smartacademyandlabsupportplatform.controller.admin;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/approve_request")
public class ApproveRequestController {
    @GetMapping()
    public String approveRequestPage(Model model, HttpSession session) {
        User sessionUser =(User) session.getAttribute("user");

        if (sessionUser == null) {
            model.addAttribute("error", "Phiên đăng nhập hết hạn!");
            return "redirect:/login";
        }
        model.addAttribute("user", sessionUser);
        return "admin/approve_request";
    }
}
