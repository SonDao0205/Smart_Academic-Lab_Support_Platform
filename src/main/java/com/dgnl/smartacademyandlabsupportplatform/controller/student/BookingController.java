package com.dgnl.smartacademyandlabsupportplatform.controller.student;

import com.dgnl.smartacademyandlabsupportplatform.model.dto.BookingDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.BookingService;
import com.dgnl.smartacademyandlabsupportplatform.service.DepartmentService;
import com.dgnl.smartacademyandlabsupportplatform.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student/booking")
public class BookingController {
    private final BookingService bookingService;
    private final DepartmentService departmentService;
    private final UserService userService;

    public BookingController(BookingService bookingService, DepartmentService departmentService, UserService userService) {
        this.bookingService = bookingService;
        this.departmentService = departmentService;
        this.userService = userService;
    }

    @GetMapping()
    public String bookingPage(@RequestParam(value = "deptId", required = false) Long deptId,
                              HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");

        model.addAttribute("user", sessionUser);
        model.addAttribute("departments", departmentService.getAll());

        if (deptId != null) {
            var lecturers = userService.getAllWithDepartment().stream()
                    .filter(l -> l.getDepartmentId() != null && l.getDepartmentId().equals(deptId))
                    .toList();
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("selectedDeptId", deptId);
        }

        model.addAttribute("bookingDTO", new BookingDTO());
        return "student/booking";
    }

    @PostMapping("/submit")
    public String submitBooking(@Valid @ModelAttribute("bookingDTO") BookingDTO bookingDTO, // Thêm @Valid
                                BindingResult bindingResult,
                                HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");

        if (bindingResult.hasErrors()) {
            prepareModelData(model, sessionUser, bookingDTO);
            return "student/booking";
        }

        try {
            bookingService.createBooking(bookingDTO, sessionUser);
            return "redirect:/student/booking?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            prepareModelData(model, sessionUser, bookingDTO);
            return "student/booking";
        }
    }

    private void prepareModelData(Model model, User sessionUser, BookingDTO bookingDTO) {
        model.addAttribute("user", sessionUser);
        model.addAttribute("departments", departmentService.getAll());
        if (bookingDTO.getDepartmentId() != null) {
            var lecturers = userService.getAllWithDepartment().stream()
                    .filter(l -> l.getDepartmentId() != null && l.getDepartmentId().equals(bookingDTO.getDepartmentId()))
                    .toList();
            model.addAttribute("lecturers", lecturers);
            model.addAttribute("selectedDeptId", bookingDTO.getDepartmentId());
        }
    }
}
