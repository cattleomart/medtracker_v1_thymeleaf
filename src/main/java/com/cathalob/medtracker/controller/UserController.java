package com.cathalob.medtracker.controller;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String gatRegistrationPage(Model model){
        model.addAttribute("user", new UserModel());
        return "registration_page";
    }
@PostMapping("/registration")
    public String registerUser(@ModelAttribute UserModel user){
        log.info("registration post");
        userService.register(user);
        return "redirect:/login_page?success";
}


}
