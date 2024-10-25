package com.cathalob.medtracker.service;

import com.cathalob.medtracker.Evaluation;
import com.cathalob.medtracker.EvaluationEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class EvaluationDataService {

    private final Map<String, Evaluation> db = new HashMap<>(){{
        put("1",new Evaluation("2024/12/"));
    }};

    public EvaluationDataService() {
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
            listData.add(Arrays.asList(entry.getDate(), entry.getBloodPressureSystole()));
        }

        log.info("getEvaluationData completed ");
        log.info("listdata", listData.toString());
        return listData;
    }

    public void importEvaluation(MultipartFile excelFile) throws IOException {
        log.info("filename: " + excelFile.getOriginalFilename());
        Evaluation evaluation = new Evaluation();
        db.put("1", evaluation);

        List<EvaluationEntry> entries = new ArrayList<EvaluationEntry>();
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            EvaluationEntry entry = new EvaluationEntry();
            log.info(""+ i);
            XSSFRow row = worksheet.getRow(i);
            Date dateCellValue = row.getCell(0).getDateCellValue();
            entry.setDate(formatDate(dateCellValue));
            XSSFCell bpCell = row.getCell(7);
            if (bpCell != null){
            entry.setBloodPressureSystole(convertedBPStringValue(bpCell.getStringCellValue()));}
            entries.add(entry);
        }
        evaluation.setEntries(entries);
    }
    private Integer convertedBPStringValue(String stringValue){
        String[] timeAndBP = stringValue.split(" - ");
        String[] bp = timeAndBP[1].split("/");
        String sys = bp[0];
       return Integer.valueOf(sys);
    }

    private String formatDate(Date date){
        SimpleDateFormat sdf =
                new SimpleDateFormat("yy-MM-dd");
        return sdf.format(date);
    }
    }
