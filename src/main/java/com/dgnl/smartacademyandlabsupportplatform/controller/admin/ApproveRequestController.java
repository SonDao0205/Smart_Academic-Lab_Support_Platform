package com.dgnl.smartacademyandlabsupportplatform.controller.admin;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.Borrowing;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.BorrowingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/approve_request")
public class ApproveRequestController {
    private final BorrowingService borrowingService;

    public ApproveRequestController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @GetMapping()
    public String approveRequestPage(Model model, HttpSession session) {
        User sessionUser =(User) session.getAttribute("user");

        if (sessionUser == null) {
            model.addAttribute("error", "Phiên đăng nhập hết hạn!");
            return "redirect:/login";
        }
        model.addAttribute("user", sessionUser);
        model.addAttribute("requests", borrowingService.getPendingRequests());
        return "admin/approve_request";
    }

    @GetMapping("/detail/{id}")
    public String detailPage(@PathVariable Long id, Model model, HttpSession session) {
        User sessionUser =(User) session.getAttribute("user");

        if (sessionUser == null) {
            model.addAttribute("error", "Phiên đăng nhập hết hạn!");
            return "redirect:/login";
        }
        model.addAttribute("user", sessionUser);
        if(id == null){
            return "redirect:/admin/approve_request";
        }
        Borrowing borrowing = borrowingService.getById(id);
        model.addAttribute("borrowing", borrowing);
        return "admin/request_detail";
    }

    @GetMapping("/approve/{detailId}")
    public String approve(@PathVariable Long detailId, RedirectAttributes ra, Model model, HttpSession session) {
        User sessionUser =(User) session.getAttribute("user");

        if (sessionUser == null) {
            model.addAttribute("error", "Phiên đăng nhập hết hạn!");
            return "redirect:/login";
        }
        model.addAttribute("user", sessionUser);
        if(detailId == null){
            return "redirect:/admin/approve_request";
        }
        try {
            borrowingService.approveDetail(detailId);
            ra.addFlashAttribute("success", "Đã duyệt thiết bị!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/approve_request";
    }

    @GetMapping("/reject/{detailId}")
    public String reject(@PathVariable Long detailId, RedirectAttributes ra, Model model, HttpSession session) {
        User sessionUser =(User) session.getAttribute("user");

        if (sessionUser == null) {
            model.addAttribute("error", "Phiên đăng nhập hết hạn!");
            return "redirect:/login";
        }
        model.addAttribute("user", sessionUser);
        if(detailId == null){
            return "redirect:/admin/approve_request";
        }
        try {
            borrowingService.rejectDetail(detailId);
            ra.addFlashAttribute("success", "Đã từ chối duyệt thiết bị!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/approve_request";
    }
}
