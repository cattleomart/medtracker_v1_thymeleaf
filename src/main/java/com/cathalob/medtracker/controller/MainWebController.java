package com.cathalob.medtracker.controller;

import com.cathalob.medtracker.service.GraphDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainWebController {
    @Autowired
    GraphDataService graphDataService;
    @GetMapping("/")
    public String index(Model model) {

        log.info("Index started");
        model.addAttribute("graphPageTitle", "Medication evaluation graph page");
        model.addAttribute("graphTitle", "Medication example graph");
        model.addAttribute("col0", "Date");
        model.addAttribute("col1", "Tom");
        model.addAttribute("col2", "Fred");

        model.addAttribute("chartData", graphDataService.getGraphData());


        log.info("Index complete");

        return "index";


    }

}
