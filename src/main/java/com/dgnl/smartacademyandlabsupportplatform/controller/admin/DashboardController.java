package com.dgnl.smartacademyandlabsupportplatform.controller.admin;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.DashboardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String index(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        model.addAttribute("user", sessionUser);
        model.addAllAttributes(dashboardService.getAdminStats());
        return "admin/dashboard";
    }
}
