package com.dgnl.smartacademyandlabsupportplatform.controller.lecturer;

import com.dgnl.smartacademyandlabsupportplatform.model.dto.AcademyEvaluationDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Equipment;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lab;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.AcademyEvaluationService;
import com.dgnl.smartacademyandlabsupportplatform.service.BookingService;
import com.dgnl.smartacademyandlabsupportplatform.service.EquipmentService;
import com.dgnl.smartacademyandlabsupportplatform.service.LabService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/lecturer/evaluation")
public class AcademyEvaluationController {
    private final AcademyEvaluationService academyEvaluationService;
    private final LabService labService;
    private final EquipmentService equipmentService;
    private final BookingService bookingService;

    public AcademyEvaluationController(AcademyEvaluationService academyEvaluationService, LabService labService, EquipmentService equipmentService, BookingService bookingService) {
        this.academyEvaluationService = academyEvaluationService;
        this.labService = labService;
        this.equipmentService = equipmentService;
        this.bookingService = bookingService;
    }

    @GetMapping()
    public String showEvaluationForm(@RequestParam("sessionId") Long sessionId,
                                     @RequestParam(value = "labId", required = false) Long labId,
                                     Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        List<Equipment> listEquipment = new ArrayList<>();
        AcademyEvaluationDTO dto = new AcademyEvaluationDTO();
        dto.setMentoringSessionId(sessionId);
        dto.setLabId(labId);

        model.addAttribute("user", sessionUser);
        model.addAttribute("labs", labService.getAll());
        model.addAttribute("sessionId", sessionId);

        if (labId != null) {
            listEquipment =equipmentService.getByLab(labId);
        }
        model.addAttribute("equipments", listEquipment);

        model.addAttribute("academyEvaluationDTO", dto);
        return "lecturer/evaluation";
    }

    @PostMapping("/add")
    public String addEvaluation(@Valid @ModelAttribute("academyEvaluationDTO") AcademyEvaluationDTO dto,
                                BindingResult bindingResult,
                                RedirectAttributes ra,
                                Model model, HttpSession session) {

        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("error", "Vui lòng nhập đầy đủ nội dung đánh giá!");
            return "redirect:/lecturer/evaluation?sessionId=" + dto.getMentoringSessionId();
        }

        try {
            if(dto.getSelectedEquipments() != null) {
                dto.getSelectedEquipments().removeIf(e -> e.getQuantity() == null || e.getQuantity() <= 0);
            }
            academyEvaluationService.save(dto);
            ra.addFlashAttribute("success", "Lưu đánh giá và tạo phiếu mượn thành công!");

            // SỬA TẠI ĐÂY: Redirect về đúng URL của trang danh sách chờ
            return "redirect:/lecturer/waiting-list";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/lecturer/evaluation?sessionId=" + dto.getMentoringSessionId();
        }
    }
}
