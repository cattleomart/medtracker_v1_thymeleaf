package com.cathalob.medtracker.controller;

import com.cathalob.medtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class AdminController {
    @Autowired
    UserService userService;

    @GetMapping("/admin/practitionerRoleRequests")
    public String practitionerRoleRequests(){
        return "admin/practitionerRoleRequests";
    }

//    @PostMapping("/admin/approvePractitionerRoleRequest")
//    public String practitionerRoleRequest(Authentication authentication){
//        UserModel userModel = userService.findByLogin(authentication.getName());
//
//        userModel.setRole(UserRole.PRACT);
//        userService.saveUser(userModel);
//        return "admin/practitionerRoleRequests?requestApproved";
//    }

}