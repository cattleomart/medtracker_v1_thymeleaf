package com.cathalob.medtracker.service;


import com.cathalob.medtracker.model.EvaluationEntry;
import com.cathalob.medtracker.repository.EvaluationEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class EvaluationDataService {

    private final EvaluationEntryRepository evaluationEntryRepository;

    public EvaluationDataService(EvaluationEntryRepository medTrackerRepository) {
        this.evaluationEntryRepository = medTrackerRepository;
    }

    public Iterable<EvaluationEntry> getEvaluationEntries(){
        return evaluationEntryRepository.findAll();
    }

    public List<List<Object>> getSystoleEvaluationData(){
        log.info("getSysEvaluationData started ");
        List<List<Object>> listData = new ArrayList<>();
        for (EvaluationEntry entry : getEvaluationEntries()) {
            listData.add(Arrays.asList(entry.getRecordDate(), entry.getBloodPressureSystole(), EvaluationEntry.BpSystoleUpperBound,
                    EvaluationEntry.BpSystoleLowerBound));
        }

        log.info("getSysEvaluationData completed ");
        return listData;
    }public List<List<Object>> getDiastoleEvaluationData(){
        log.info("getDiaEvaluationData started ");
        List<List<Object>> listData = new ArrayList<>();
        for (EvaluationEntry entry : getEvaluationEntries()) {
            listData.add(Arrays.asList(entry.getRecordDate(), entry.getBloodPressureDiastole(), EvaluationEntry.BpDiastoleUpperBound,
                    EvaluationEntry.BpDiastoleLowerBound));
        }
        log.info("getDiaEvaluationData completed ");
        return listData;
    }

    public void importEvaluation(MultipartFile excelFile) throws IOException {
        String originalFilename = excelFile.getOriginalFilename();
        log.info("filename: " + originalFilename);

        List<EvaluationEntry> entries = new ArrayList<EvaluationEntry>();
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            EvaluationEntry entry = new EvaluationEntry();
            log.info(""+ i);
            XSSFRow row = worksheet.getRow(i);

            Date dateCellValue = row.getCell(0).getDateCellValue();
            entry.setRecordDate(dateCellValue);
            XSSFCell bpSystoleCell = row.getCell(8);
            if (bpSystoleCell != null){
                entry.setBloodPressureSystole((((int) bpSystoleCell.getNumericCellValue())));
            }
            XSSFCell bpDiastoleCell = row.getCell(9);
            if (bpDiastoleCell != null){
                entry.setBloodPressureDiastole((((int) bpDiastoleCell.getNumericCellValue())));
            }
            XSSFCell bpHeartRateCell = row.getCell(10);
            if (bpHeartRateCell != null){
                entry.setHeartRate((((int) bpHeartRateCell.getNumericCellValue())));
            }

            entries.add(entry);
        }
        evaluationEntryRepository.saveAll(entries);
    }

    public String getEvaluationOriginFilename(){
        return "Deprecated";
    }
    }
