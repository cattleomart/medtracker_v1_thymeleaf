package com.cathalob.medtracker.service;


import com.cathalob.medtracker.model.EvaluationEntry;
import com.cathalob.medtracker.model.UserModel;
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

    public Iterable<EvaluationEntry> getEvaluationEntries(UserModel userModel){
        return evaluationEntryRepository.findEvaluationEntriesForUserId(userModel.getId());
    }

    public List<List<Object>> getSystoleEvaluationData(Iterable<EvaluationEntry> evaluationEntries){
        log.info("getSysEvaluationData started ");
        List<List<Object>> listData = new ArrayList<>();
        for (EvaluationEntry entry : evaluationEntries) {
            listData.add(Arrays.asList(entry.getRecordDate(), entry.getBloodPressureSystole(), EvaluationEntry.BpSystoleUpperBound,
                    EvaluationEntry.BpSystoleLowerBound));
        }

        log.info("getSysEvaluationData completed ");
        return listData;
    }public List<List<Object>> getDiastoleEvaluationData(Iterable<EvaluationEntry> evaluationEntries){
        log.info("getDiaEvaluationData started ");
        List<List<Object>> listData = new ArrayList<>();
        for (EvaluationEntry entry : evaluationEntries) {
            listData.add(Arrays.asList(entry.getRecordDate(), entry.getBloodPressureDiastole(), EvaluationEntry.BpDiastoleUpperBound,
                    EvaluationEntry.BpDiastoleLowerBound));
        }
        log.info("getDiaEvaluationData completed ");
        return listData;
    }

    public void importEvaluation(MultipartFile excelFile, UserModel userModel) throws IOException {
        String originalFilename = excelFile.getOriginalFilename();
        log.info("filename: " + originalFilename);

        List<EvaluationEntry> entries = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            EvaluationEntry entry = new EvaluationEntry();
            entry.setUserModel(userModel);
            log.info(""+ i);
            XSSFRow row = worksheet.getRow(i);

            Date dateCellValue = row.getCell(0).getDateCellValue();
            entry.setRecordDate(dateCellValue);
            XSSFCell bpSystoleCell = row.getCell(9);
            if (bpSystoleCell != null){
                entry.setBloodPressureSystole((((int) bpSystoleCell.getNumericCellValue())));
            }
            XSSFCell bpDiastoleCell = row.getCell(10);
            if (bpDiastoleCell != null){
                entry.setBloodPressureDiastole((((int) bpDiastoleCell.getNumericCellValue())));
            }
            XSSFCell bpHeartRateCell = row.getCell(11);
            if (bpHeartRateCell != null){
                entry.setHeartRate((((int) bpHeartRateCell.getNumericCellValue())));
            }

            if (entry.hasData()) {
            entries.add(entry);
            }
        }
        evaluationEntryRepository.saveAll(entries);
    }


    }
