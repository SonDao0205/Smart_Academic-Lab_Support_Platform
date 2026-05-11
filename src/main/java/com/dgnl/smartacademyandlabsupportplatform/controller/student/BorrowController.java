package com.dgnl.smartacademyandlabsupportplatform.controller.student;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.Borrowing;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.BorrowingDetail;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.BorrowingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student/borrow_devices")
public class BorrowController {
    private final BorrowingService borrowingService;

    public BorrowController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @GetMapping()
    public String borrowDevices(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        List<BorrowingDetail> details = borrowingService.getDetailsByUserId(sessionUser.getId());

        model.addAttribute("user", sessionUser);
        model.addAttribute("details", details);

        return "student/borrow_devices";
    }
}
