package com.cathalob.medtracker.service;

import com.cathalob.medtracker.Evaluation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class GraphDataService {
    public List<List<Object>> getGraphData(Evaluation evaluation)
    {
        log.info("getGraphData started ");


        List<List<Object>> listData = Arrays.asList(Arrays.asList("01/10/24", 2, 2),Arrays.asList("01/11/24", 4, 5), Arrays.asList("01/12/24", 8, 9) );

        log.info("getGraphData completed ");
        return listData;
    }
}
