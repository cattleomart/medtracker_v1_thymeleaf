package com.cathalob.medtracker.controller;

import com.cathalob.medtracker.dto.PractitionerRoleRequestsDTO;
import com.cathalob.medtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
        return "admin/practitionerRoleRequests";
    }

    @PostMapping("/admin/approvePractitionerRoleRequests")
    public String approvePractitionerRoleRequests(Authentication authentication, @ModelAttribute PractitionerRoleRequestsDTO requestsDTO, Model model) {
        userService.approvePractitionerRoleRequests(authentication.getName(), requestsDTO.requests);
        return "admin/practitionerRoleRequests?success";
    }

}