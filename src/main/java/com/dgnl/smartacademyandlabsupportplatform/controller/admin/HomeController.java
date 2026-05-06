package com.dgnl.smartacademyandlabsupportplatform.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("admin_controller")
@RequestMapping("/admin")
public class HomeController {
    @GetMapping()
    public String home() {
        return "redirect:/admin/profile";
    }
}
