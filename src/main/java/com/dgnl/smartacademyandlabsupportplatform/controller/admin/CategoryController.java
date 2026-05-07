package com.dgnl.smartacademyandlabsupportplatform.controller.admin;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.DepartmentService;
import com.dgnl.smartacademyandlabsupportplatform.service.EquipmentService;
import com.dgnl.smartacademyandlabsupportplatform.service.LabService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    private final DepartmentService  departmentService;
    private final LabService labService;
    public CategoryController(DepartmentService departmentService, LabService labService) {
        this.departmentService = departmentService;
        this.labService = labService;
    }

    @GetMapping("/department")
    public String departmentPage(Model model, HttpSession session) {
        User sessionUser =(User) session.getAttribute("user");

        if (sessionUser == null) {
            model.addAttribute("error", "Phiên đăng nhập hết hạn!");
            return "redirect:/login";
        }
        model.addAttribute("user", sessionUser);
        model.addAttribute("departments",departmentService.getAll());
        return "admin/department";
    }

    @GetMapping("/lab")
    public String labPage(Model model, HttpSession session) {
        User sessionUser =(User) session.getAttribute("user");

        if (sessionUser == null) {
            model.addAttribute("error", "Phiên đăng nhập hết hạn!");
            return "redirect:/login";
        }
        model.addAttribute("user", sessionUser);
        model.addAttribute("labs",labService.getAll());
        return "admin/lab";
    }


}
