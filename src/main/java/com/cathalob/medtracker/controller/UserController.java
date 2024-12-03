package com.cathalob.medtracker.controller;

import com.cathalob.medtracker.dto.UserModelDTO;

import com.cathalob.medtracker.model.PractitionerRoleRequest;
import com.cathalob.medtracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j

public class UserController {
    private final UserService userService;
    @GetMapping("/login")
    public String getLoginPage(){
        return "login_page";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model){
        model.addAttribute("user", new UserModelDTO());
        return "registration_page";
    }
    @PostMapping("/registration")
    public String registerUser(@ModelAttribute @Valid UserModelDTO user ){
        log.info("registration post");

        userService.register(user);
        return "redirect:/login_page?success";
    }
    @GetMapping("/user/practitionerRoleRequest")
    public String practitionerRoleRequest(Model model, Authentication authentication){

        PractitionerRoleRequest practitionerRoleRequest = userService.getPractitionerRoleRequest(authentication.getName());
        if (practitionerRoleRequest != null) {
        model.addAttribute("pending", practitionerRoleRequest.isPending());
        model.addAttribute("approved", practitionerRoleRequest.isApproved());
        model.addAttribute("notSubmitted", (false));
        } else {
            model.addAttribute("pending", false);
            model.addAttribute("approved", false);
            model.addAttribute("notSubmitted", (true));
        }
        return "user/practitionerRoleRequest";
    }

    @GetMapping("/user/accountManagement")
    public String getAccountManagement(){
        return "user/accountManagement";
    }
    @PostMapping("/user/accountManagement/practitionerRoleRequest")
    public String practitionerRoleRequest(Authentication authentication){
        userService.submitPractitionerRoleRequest(authentication.getName());
        return "redirect:/user/practitionerRoleRequest?success";
    }
    @PostMapping("/user/accountManagement/change_password")
    public String passwordChangeRequest(Authentication authentication){
        return "redirect:/user/accountManagement?" + (userService.submitPasswordChangeRequest() ? "success" : "error");
    }

}
