package com.dgnl.smartacademyandlabsupportplatform.controller.admin;

import com.dgnl.smartacademyandlabsupportplatform.exception.GetById;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.EquipmentDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Equipment;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.EquipmentService;
import com.dgnl.smartacademyandlabsupportplatform.service.LabService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/equipment")
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final LabService labService;
    public EquipmentController(EquipmentService equipmentService, LabService labService) {
        this.equipmentService = equipmentService;
        this.labService = labService;
    }

    @GetMapping()
    public String equipmentPage(Model model, HttpSession session) {
        User sessionUser =(User) session.getAttribute("user");

        model.addAttribute("user", sessionUser);
        model.addAttribute("equipDTO", new EquipmentDTO());
        model.addAttribute("equipments", equipmentService.getAll());
        model.addAttribute("labs", labService.getAll());
        return "admin/equipment";
    }

    @PostMapping("/add")
    public String addEquipment(@Valid @ModelAttribute("equipDTO") EquipmentDTO equipmentDTO,
                               BindingResult bindingResult,
                               Model model,
                               HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");


        if (bindingResult.hasErrors()) {
            model.addAttribute("user", sessionUser);
            model.addAttribute("equipments", equipmentService.getAll());
            model.addAttribute("labs", labService.getAll());
            model.addAttribute("hasErrors", true);

            if (equipmentDTO.getId() != null) {
                model.addAttribute("isEdit", true);
            }

            return "admin/equipment";
        }

        equipmentService.save(equipmentDTO);

        String message = (equipmentDTO.getId() == null)
                ? "Thêm thiết bị thành công!"
                : "Cập nhật thiết bị thành công!";
        model.addAttribute("success", message);

        return "redirect:/admin/equipment";
    }

    @GetMapping("/delete/{id}")
    public String deleteEquipment(@PathVariable Integer id, HttpSession session, Model model) {
        User sessionUser =(User) session.getAttribute("user");

        model.addAttribute("user", sessionUser);
        equipmentService.deleteById(id);
        model.addAttribute("success", "Xoá thiết bị/tài liệu thành công!");
        return "redirect:/admin/equipment";
    }

    @GetMapping("/edit/{id}")
    public String editEquipment(@PathVariable Integer id, HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");

        try {
            Equipment equipment = equipmentService.getById(id);

            EquipmentDTO dto = new EquipmentDTO();
            dto.setId(equipment.getId());
            dto.setName(equipment.getName());
            dto.setDescription(equipment.getDescription());
            dto.setQuantity(equipment.getQuantity());
            dto.setLab(equipment.getLab());

            model.addAttribute("user", sessionUser);
            model.addAttribute("equipDTO", dto);
            model.addAttribute("equipments", equipmentService.getAll());
            model.addAttribute("labs", labService.getAll());

            model.addAttribute("hasErrors", true);
            model.addAttribute("isEdit", true);

            return "admin/equipment";
        } catch (GetById e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/equipment";
        }
    }
}
