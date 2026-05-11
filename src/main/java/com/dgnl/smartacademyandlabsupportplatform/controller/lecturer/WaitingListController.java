package com.dgnl.smartacademyandlabsupportplatform.controller.lecturer;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lecturer/waiting-list")
public class WaitingListController {

    private final BookingService bookingService;

    public WaitingListController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public String waitingListPage(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");

        var list = bookingService.getLecturerWaitingList(sessionUser.getId());
        model.addAttribute("user", sessionUser);
        model.addAttribute("waitingList", list);
        return "lecturer/student_in_wait";
    }

    @GetMapping("/complete/{id}")
    public String complete(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        User sessionUser = (User) session.getAttribute("user");
        try {
            bookingService.completeSession(id, sessionUser.getId());
            ra.addFlashAttribute("success", "Xác nhận hoàn thành buổi tư vấn!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/lecturer/waiting-list";
    }

    @GetMapping("/reject/{id}")
    public String reject(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        User sessionUser = (User) session.getAttribute("user");

        try {
            bookingService.rejectSession(id, sessionUser.getId());
            ra.addFlashAttribute("success", "Đã từ chối buổi tư vấn thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/lecturer/waiting-list";
    }
}
