package com.dgnl.smartacademyandlabsupportplatform.controller.lecturer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("lecturer_controller")
@RequestMapping("/lecturer")
public class HomeController {
    @GetMapping()
    public String home() {
        return "lecturer/student_in_wait";
    }
}
