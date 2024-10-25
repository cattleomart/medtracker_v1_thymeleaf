package com.cathalob.medtracker.service;

import com.cathalob.medtracker.Evaluation;
import com.cathalob.medtracker.EvaluationEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EvaluationDataService {

    private final Map<String, Evaluation> db = new HashMap<>(){{
        put("1",new Evaluation("2024/12/"));
    }};

    public EvaluationDataService() {
    }

    public void uploadEvaluation(String fileName, String contentType, byte[] data){
        log.info("filename: " + fileName);
        log.info("contentType: " + contentType);
        log.info("Data " + data.toString());

        db.put("1", new Evaluation("2024/12/"));
        log.info("Uploaded file");
    }

    public Evaluation getEvaluation(){
        Evaluation evaluation = db.get("1");
        return evaluation;
    }
    public List<List<Object>> getEvaluationData(){
        Evaluation evaluation = getEvaluation();
        log.info("getEvaluationData started ");
        List<List<Object>> listData = new ArrayList<>();
        for (EvaluationEntry entry : evaluation.getEntries()) {
            listData.add(Arrays.asList(entry.getDate(), entry.getRecordedValue()));
        }

        log.info("getEvaluationData completed ");
        log.info("listdata", listData.toString());
        return listData;
    }
}
