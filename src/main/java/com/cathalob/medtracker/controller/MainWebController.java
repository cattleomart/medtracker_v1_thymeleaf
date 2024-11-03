package com.cathalob.medtracker.controller;
import com.cathalob.medtracker.model.EvaluationEntry;
import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.service.EvaluationDataService;
import com.cathalob.medtracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Slf4j
public class MainWebController {

@Autowired
   EvaluationDataService evaluationDataService;

@Autowired
 UserService userService;

    public MainWebController() {

    }
    @GetMapping("/")
    public String index() {
        log.info("Index started");
        log.info("Index complete");
        return "index";
    }

    @GetMapping("/upload")
    public String upload(){
        return "upload";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }
    @PostMapping("/upload")
    public String mapReapExcelData(@RequestParam("file") MultipartFile reapExcelDataFile, Authentication authentication) throws IOException {
     evaluationDataService.importEvaluation(reapExcelDataFile, getUserModel(authentication));
        return "upload";
    }

 private UserModel getUserModel(Authentication authentication) {
  return userService.findByLogin(authentication.getName());
 }

 @GetMapping("/graphs")
    public String graphs(Model model, Authentication authentication){
  Iterable<EvaluationEntry> evaluationEntries = evaluationDataService.getEvaluationEntries(getUserModel(authentication));
  log.info("graphs started");

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



    model.addAttribute("systoleChartData",evaluationDataService.getSystoleEvaluationData(evaluationEntries));
    model.addAttribute("diastoleChartData",evaluationDataService.getDiastoleEvaluationData(evaluationEntries));
    log.info("graphs complete");
    return "graphs";
    }









}
