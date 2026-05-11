package com.dgnl.smartacademyandlabsupportplatform.controller.student;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.AcademyEvaluation;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.AcademyEvaluationService;
import com.dgnl.smartacademyandlabsupportplatform.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student/history")
public class HistoryController {
    private final BookingService bookingService;
    private final AcademyEvaluationService academyEvaluationService;

    public HistoryController(BookingService bookingService, AcademyEvaluationService academyEvaluationService) {
        this.bookingService = bookingService;
        this.academyEvaluationService = academyEvaluationService;
    }

    @GetMapping()
    public String historyPage(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        model.addAttribute("user", sessionUser);
        var history = bookingService.getHistoryByStudent(sessionUser.getId());
        model.addAttribute("history", history);
        return "student/history";
    }

    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        User sessionUser = (User) session.getAttribute("user");

        try {
            bookingService.cancelBooking(id, sessionUser.getId());
            ra.addFlashAttribute("success", "Đã hủy lịch hẹn thành công!");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/history";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long sessionId, HttpSession session, Model model, RedirectAttributes ra) {
        User sessionUser = (User) session.getAttribute("user");

        var mentoringSession = bookingService.getById(sessionId);

        AcademyEvaluation evaluation = academyEvaluationService.getByMentoringSessionId(sessionId);

        if (evaluation == null) {
            ra.addFlashAttribute("error", "Hồ sơ đánh giá hiện chưa có hoặc chưa được cập nhật.");
            return "redirect:/student/history";
        }

        model.addAttribute("user", sessionUser);
        model.addAttribute("sessionDetail", mentoringSession);
        model.addAttribute("evaluation", evaluation);

        return "student/evaluation_detail";
    }
}