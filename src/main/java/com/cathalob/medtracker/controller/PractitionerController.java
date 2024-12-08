package com.cathalob.medtracker.controller;

import com.cathalob.medtracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PractitionerController {
    private final UserService userService;

    public PractitionerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/practitioner/patients")
    public String getPatients(Model model) {
        model.addAttribute("users", userService.getPatientUserModels());
        return "practitioner/patientsList";
    }


}
