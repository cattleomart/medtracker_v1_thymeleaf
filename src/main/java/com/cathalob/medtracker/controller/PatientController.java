package com.cathalob.medtracker.controller;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.tracking.Dose;
import com.cathalob.medtracker.service.PatientsService;
import com.cathalob.medtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class PatientController {
    @Autowired
    PatientsService patientsService;

    @Autowired
    UserService userService;
    @GetMapping("/patient/prescriptions")
    public String getPatients(Model model){
        model.addAttribute("prescriptionsDTO", patientsService.getPrescriptionsDTO()  );
        return "patient/prescriptionsList";
    }

    private UserModel getUserModel(Authentication authentication) {
        return userService.findByLogin(authentication.getName());
    }

    @GetMapping("/patient/graphs")
    public String graphs(Model model, Authentication authentication) {
        {


            model.addAttribute("graphPageTitle", "Data Visualizations");
            model.addAttribute("bpSectionTitle", "Blood pressure");
            model.addAttribute("col0", "Date");

            model.addAttribute("systoleGraphTitle", "Systole");
            model.addAttribute("col1", "Morning");
            model.addAttribute("col2", "Lunch");
            model.addAttribute("col3", "Second Dose Peak");
            model.addAttribute("col4", "Danger High");
            model.addAttribute("col5", "High Stage 1");
            model.addAttribute("col6", "Elevated");


            model.addAttribute("diastoleGraphTitle", "Diastole");
            model.addAttribute("dcol1", "Morning");
            model.addAttribute("dcol2", "Lunch");
            model.addAttribute("dcol3", "Second Dose Peak");
            model.addAttribute("dcol4", "Danger High");
            model.addAttribute("dcol5", "High Stage 1");

            List<List<Object>> doseGraphData = patientsService.getDoseGraphData(this.getUserModel(authentication));

            model.addAttribute("doseGraphTitle", "Dose (mg)");
            model.addAttribute("colls", (patientsService.getDoseGraphColumnNames(this.getUserModel(authentication))));


            model.addAttribute("systoleChartData", Arrays.asList(1,2,3,4,5,6));
            model.addAttribute("diastoleChartData", Arrays.asList(1,2,3,4,5));
            model.addAttribute("doseChartData", doseGraphData);


            return "graphs";
        }
    }
}
