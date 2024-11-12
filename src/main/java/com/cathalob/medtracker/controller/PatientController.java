package com.cathalob.medtracker.controller;

import com.cathalob.medtracker.service.PatientsService;
import com.cathalob.medtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientController {
    @Autowired
    PatientsService patientsService;
    @GetMapping("/patient/prescriptions")
    public String getPatients(Model model){
        model.addAttribute("prescriptionsDTO", patientsService.getPrescriptionsDTO()  );
        return "patient/prescriptionsList";
    }

}
