package com.cathalob.medtracker.controller;

import com.cathalob.medtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PractitionerController {
    @Autowired
    UserService userService;

    @GetMapping("/practitioner/patients")
    public String getPatients(Model model){
        model.addAttribute("users", userService.getUserModels());
        return "practitioner/patientsList";
    }


}
