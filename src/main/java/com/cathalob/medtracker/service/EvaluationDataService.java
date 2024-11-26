package com.cathalob.medtracker.service;


import com.cathalob.medtracker.err.ExcelImportException;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class EvaluationDataService {

    private final EvaluationEntryRepository evaluationEntryRepository;

    public EvaluationDataService(EvaluationEntryRepository medTrackerRepository) {
        this.evaluationEntryRepository = medTrackerRepository;
    }

    public Iterable<EvaluationEntry> getEvaluationEntries(UserModel userModel) {
        return evaluationEntryRepository.findEvaluationEntriesForUserId(userModel.getId());
    }


    private void processEvaluationEntries(List<EvaluationEntry> entries, UserModel userModel) {

        Map<Date, EvaluationEntry> existingMap = StreamSupport.stream(getEvaluationEntries(userModel).spliterator(), false).collect(Collectors.toMap(EvaluationEntry::getRecordDate, Function.identity()));

        List<EvaluationEntry> validEntries = new ArrayList<>();
        entries.forEach(entry -> {
            if (entry.hasData()) {

                EvaluationEntry existing = existingMap.get(entry.getRecordDate());
                if (existing != null) {
                    log.info("Found existing entry: " + existing.getRecordDate());
                    entry.setId(existing.getId());
                } else {
                    log.info("New entry: " + entry.getRecordDate());
                }
                validEntries.add(entry);
            }
        });

        evaluationEntryRepository.saveAll(validEntries);
    }


    public List<List<Object>> getSystoleEvaluationData(Iterable<EvaluationEntry> evaluationEntries) {
        log.info("getSysEvaluationData started ");
        List<List<Object>> listData = new ArrayList<>();
        for (EvaluationEntry entry : evaluationEntries) {
            listData.add(Arrays.asList(formattedDateOfEntry(entry), entry.getBloodPressureSystole(), entry.getLunchBloodPressureSystole(), entry.getSdpBloodPressureSystole(), EvaluationEntry.BpSystoleUpperBound,
//                    EvaluationEntry.BpSystoleLowerBound,
                    130, 120));
        }

        log.info("getSysEvaluationData completed ");
        return listData;
    }

    public List<List<Object>> getDiastoleEvaluationData(Iterable<EvaluationEntry> evaluationEntries) {
        log.info("getDiaEvaluationData started ");
        List<List<Object>> listData = new ArrayList<>();
        for (EvaluationEntry entry : evaluationEntries) {
            listData.add(Arrays.asList(formattedDateOfEntry(entry), entry.getBloodPressureDiastole(), entry.getLunchBloodPressureDiastole(), entry.getSdpBloodPressureDiastole(), EvaluationEntry.BpDiastoleUpperBound, 80

            ));
        }
        log.info("getDiaEvaluationData completed ");
        return listData;
    }

    public List<String> drugNames(Iterable<EvaluationEntry> evaluationEntries){
        List<String> names = new ArrayList<>();
        evaluationEntries.forEach((e)->names.add(e.getMedication()));
        return (names.stream().distinct().toList());
    }
    public List<List<Object>> getDoseEvaluationData(Iterable<EvaluationEntry> evaluationEntries) {
        log.info("getDoseEvaluationData started ");
//        separate data by drug


        List<List<Object>> listData = new ArrayList<>();

        for (EvaluationEntry entry : evaluationEntries) {
            List<Object> dayListData = new ArrayList<>();
            dayListData.add(formattedDateOfEntry(entry));

            if (entry.getMedication().equals("Methylphenidate")) {
                dayListData.add(entry.getDose1());
                dayListData.add(entry.getDose2());
                for (int i = 0; i < 4; i++) {
                    dayListData.add(null);
                }
            } else if (entry.getMedication().equals("No Meds")) {
                for (int i = 0; i < 2; i++) {
                    dayListData.add(null);
                }
                for (int i = 0; i < 2; i++) {
                    dayListData.add(0);
                }
                for (int i = 0; i < 2; i++) {
                    dayListData.add(null);
                }
            } else if (entry.getMedication().equals("Dexamphetamine")) {
                for (int i = 0; i < 4; i++) {
                    dayListData.add(null);
                }
                dayListData.add(entry.getDose1());
                dayListData.add(entry.getDose2());
            }

//        System.out.println(dayListData);
            listData.add(dayListData);


        }
        log.info("getDoseEvaluationData completed ");
        return listData;
    }


    public void importEvaluation(MultipartFile excelFile, UserModel userModel) throws IOException, ExcelImportException {
        String originalFilename = excelFile.getOriginalFilename();
        log.info("filename: " + originalFilename);


        List<EvaluationEntry> entries = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            EvaluationEntry entry = new EvaluationEntry();
            try {
                entry.setUserModel(userModel);
                log.info("" + i);
                XSSFRow row = worksheet.getRow(i);
                if (row.getCell(0) != null) {
                    Date dateCellValue = row.getCell(0).getDateCellValue();
                    entry.setRecordDate(dateCellValue);
                }

                XSSFCell medicationCell = row.getCell(20);
                if (medicationCell != null) {
                    entry.setMedication(medicationCell.getStringCellValue());
                }

                XSSFCell dose1Cell = row.getCell(6);
                if (dose1Cell != null) {
                    entry.setDose1((((int) dose1Cell.getNumericCellValue())));
                }

                XSSFCell dose2Cell = row.getCell(13);
                if (dose2Cell != null) {
                    entry.setDose2((((int) dose2Cell.getNumericCellValue())));
                }


                XSSFCell bpSystoleCell = row.getCell(3);
                if (bpSystoleCell != null) {
                    entry.setBloodPressureSystole((((int) bpSystoleCell.getNumericCellValue())));
                }
                XSSFCell bpDiastoleCell = row.getCell(4);
                if (bpDiastoleCell != null) {
                    entry.setBloodPressureDiastole((((int) bpDiastoleCell.getNumericCellValue())));
                }
                XSSFCell bpHeartRateCell = row.getCell(5);
                if (bpHeartRateCell != null) {
                    entry.setHeartRate((((int) bpHeartRateCell.getNumericCellValue())));
                }

//            Lunch reading
                XSSFCell lunchBpSystoleCell = row.getCell(10);
                if (lunchBpSystoleCell != null) {
                    entry.setLunchBloodPressureSystole((((int) lunchBpSystoleCell.getNumericCellValue())));
                }
                XSSFCell lunchBpDiastoleCell = row.getCell(11);
                if (lunchBpDiastoleCell != null) {
                    entry.setLunchBloodPressureDiastole((((int) lunchBpDiastoleCell.getNumericCellValue())));
                }
                XSSFCell lunchBpHeartRateCell = row.getCell(12);
                if (lunchBpHeartRateCell != null) {
                    entry.setLunchHeartRate((((int) lunchBpHeartRateCell.getNumericCellValue())));
                }

//            Second Dose peak reading
                XSSFCell sdpBpSystoleCell = row.getCell(16);
                if (sdpBpSystoleCell != null) {
                    entry.setSdpBloodPressureSystole((((int) sdpBpSystoleCell.getNumericCellValue())));
                }
                XSSFCell sdpBpDiastoleCell = row.getCell(17);
                if (sdpBpDiastoleCell != null) {
                    entry.setSdpBloodPressureDiastole((((int) sdpBpDiastoleCell.getNumericCellValue())));
                }
                XSSFCell sdpBpHeartRateCell = row.getCell(18);
                if (sdpBpHeartRateCell != null) {
                    entry.setSdpHeartRate((((int) sdpBpHeartRateCell.getNumericCellValue())));
                }
                entries.add(entry);

            } catch (Exception e) {
                throw new ExcelImportException("Excel error for date: " + entry.getRecordDate() + "MSG" + e.getMessage());
            }


        }
        processEvaluationEntries(entries, userModel);
    }

    private String formattedDateOfEntry(EvaluationEntry evaluationEntry) {
        LocalDate localDate = Instant.ofEpochMilli(evaluationEntry.getRecordDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(localDate);

    }
}
