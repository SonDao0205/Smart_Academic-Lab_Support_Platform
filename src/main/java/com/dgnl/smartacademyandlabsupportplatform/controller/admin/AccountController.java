package com.dgnl.smartacademyandlabsupportplatform.controller.admin;

import com.dgnl.smartacademyandlabsupportplatform.exception.DataDuplicate;
import com.dgnl.smartacademyandlabsupportplatform.exception.GetById;
import com.dgnl.smartacademyandlabsupportplatform.exception.MissingInput;
import com.dgnl.smartacademyandlabsupportplatform.model.dto.UserDTO;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lecturer;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.DepartmentService;
import com.dgnl.smartacademyandlabsupportplatform.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/account")
public class AccountController {
    private final UserService userService;
    private final DepartmentService departmentService;

    public AccountController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    @GetMapping()
    public String accountPage(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        model.addAttribute("user", sessionUser);
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("accounts", userService.getAllWithDepartment());
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("isEdit", false);
        model.addAttribute("hasErrors", false);
        return "admin/account";
    }

    @PostMapping("/add")
    public String saveAccount(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                              BindingResult bindingResult,
                              Model model,
                              HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        if (userDTO.getId() == null && (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty())) {
            bindingResult.rejectValue("password", "error.userDTO", "Mật khẩu bắt buộc khi thêm mới!");
        }

        if (bindingResult.hasErrors()) {
            prepareModel(model, sessionUser, userDTO);
            return "admin/account";
        }

        try {
            userService.save(userDTO);
            return "redirect:/admin/account";
        } catch (DataDuplicate e) {
            bindingResult.rejectValue(e.getField(), e.getField() + "duplicate", e.getMessage());
            prepareModel(model, sessionUser, userDTO);
            return "admin/account";
        } catch (MissingInput e) {
            bindingResult.rejectValue(e.getField(), e.getField() + "missing_input", e.getMessage());
            prepareModel(model, sessionUser, userDTO);
            return "admin/account";
        }
    }

    private void prepareModel(Model model, User sessionUser, UserDTO userDTO) {
        model.addAttribute("user", sessionUser);
        model.addAttribute("accounts", userService.getAllWithDepartment());
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("hasErrors", true);
        model.addAttribute("isEdit", userDTO.getId() != null);
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        userService.deleteById(id);
        return "redirect:/admin/account";
    }

    @GetMapping("/edit/{id}")
    public String editAccount(@PathVariable Long id, HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        try {
            User user = userService.getById(id);
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setFullName(user.getFullName());
            dto.setUserCode(user.getUserCode());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setRole(user.getRole());

            if ("lecturer".equals(user.getRole())) {
                Lecturer lecturer = userService.getLecturerByUserId(user.getId());
                if (lecturer != null && lecturer.getDepartment() != null) {
                    dto.setDepartmentId(lecturer.getDepartment().getId());
                }
            }

            model.addAttribute("user", sessionUser);
            model.addAttribute("accounts", userService.getAllWithDepartment());
            model.addAttribute("departments", departmentService.getAll());
            model.addAttribute("userDTO", dto);
            model.addAttribute("hasErrors", true);
            model.addAttribute("isEdit", true);

            return "admin/account";
        } catch (GetById e) {
            return "redirect:/admin/account";
        }
    }
}