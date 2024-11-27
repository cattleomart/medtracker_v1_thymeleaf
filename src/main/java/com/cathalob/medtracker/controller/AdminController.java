package com.cathalob.medtracker.controller;

import com.cathalob.medtracker.dto.PractitionerRoleRequestsDTO;
import com.cathalob.medtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller

public class AdminController {
    @Autowired
    UserService userService;

    @GetMapping("/admin/practitionerRoleRequests")
    public String getPractitionerRoleRequests(Model model){
        model.addAttribute("requestsDTO", userService.getPractitionerRoleRequestsDTO());
        model.addAttribute("users", userService.getUserModels());
        return "admin/practitionerRoleRequests";
    }

    @PostMapping("/admin/approvePractitionerRoleRequests")
    public String approvePractitionerRoleRequests(@ModelAttribute PractitionerRoleRequestsDTO requestsDTO) {
        userService.approvePractitionerRoleRequests( requestsDTO.requests);
        return "redirect:/admin/practitionerRoleRequests?success";
    }


}