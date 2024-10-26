package com.cathalob.medtracker.controller;

import com.cathalob.medtracker.service.EvaluationDataService;
import com.cathalob.medtracker.service.GraphDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Slf4j
public class MainWebController {
    @Autowired
    GraphDataService graphDataService;
@Autowired
   EvaluationDataService evaluationDataService;

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

    //    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/upload")
    public String mapReapExcelData(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {
        evaluationDataService.importEvaluation(reapExcelDataFile);
        return "upload";
    }

    @GetMapping("/graphs")
    public String graphs(Model model){
    log.info("graphs started");

    model.addAttribute("graphPageTitle", "Evaluation Graphs");
    model.addAttribute("importedFileName", evaluationDataService.getEvaluationOriginFilename());
    model.addAttribute("bpSectionTitle", "Blood pressure");
    model.addAttribute("col0", "Date");

    model.addAttribute("systoleGraphTitle", "Systole");
    model.addAttribute("col1", "Reading");
    model.addAttribute("col2", "Upper Bound");
    model.addAttribute("col3", "Lower Bound");

    model.addAttribute("diastoleGraphTitle", "Diastole");
    model.addAttribute("col4", "Reading");
    model.addAttribute("col5", "Upper Bound");
    model.addAttribute("col6", "Lower Bound");

    model.addAttribute("systoleChartData",evaluationDataService.getSystoleEvaluationData());
    model.addAttribute("diastoleChartData",evaluationDataService.getDiastoleEvaluationData());
    log.info("graphs complete");
    return "graphs";
    }









}
