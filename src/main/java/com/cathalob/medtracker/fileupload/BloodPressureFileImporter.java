package com.cathalob.medtracker.fileupload;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.enums.DAYSTAGE;
import com.cathalob.medtracker.model.tracking.BloodPressureReading;
import com.cathalob.medtracker.service.EvaluationDataService;
import com.cathalob.medtracker.service.PatientsService;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
@Slf4j
public class BloodPressureFileImporter {
    private final UserModel userModel;
    private final ImportContext importContext;
    private final PatientsService patientsService;
    private final EvaluationDataService evaluationDataService;


    public BloodPressureFileImporter(UserModel userModel, EvaluationDataService evaluationDataService, PatientsService patientsService) {
        importContext = new ImportContext();
        this.evaluationDataService = evaluationDataService;
        this.patientsService = patientsService;
        this.userModel = userModel;
    }


    public void processFile(MultipartFile fileToImport){
        List<BloodPressureReading> newBloodPressureReadings = new ArrayList<>();
        log.info(this.getClass() + " User: " + userModel.getUsername() + " FN: " + fileToImport.getOriginalFilename());
        XSSFWorkbook workbook = null;

        try {
            workbook = new XSSFWorkbook(fileToImport.getInputStream());
//            log.info("Number of sheets: " + workbook.getNumberOfSheets());

            workbook.forEach(sheet -> {
//                log.info("Title of sheet => " + sheet.getSheetName());

                int index = 0;
                for (Row row : sheet) {
                    if (index++ == 0) continue;
                    BloodPressureReading bloodPressureReading = new BloodPressureReading();
                    LocalDate localDate = LocalDate.now();
                    LocalTime localTime = LocalTime.now();

                    if (row.getCell(0) != null) {
                        localDate = LocalDate.ofInstant(
                                row.getCell(0).getDateCellValue().toInstant(), ZoneId.systemDefault());
                    }

                    if (row.getCell(1) != null) {
                        String dayStage = row.getCell(1).getStringCellValue();
                        bloodPressureReading.setDayStage(DAYSTAGE.valueOf(dayStage));
                    }
                    int timeCellIndex = 2;
                    if (row.getCell(timeCellIndex) != null && (row.getCell(timeCellIndex).getLocalDateTimeCellValue() != null)) {
                        localTime = (row.getCell(timeCellIndex).getLocalDateTimeCellValue().toLocalTime());
                    }

                    int systoleIndex = 3;
                    if (row.getCell(systoleIndex) != null) {
                        int numericCellValue = (int) (row.getCell(systoleIndex).getNumericCellValue());
                        bloodPressureReading.setSystole(numericCellValue);
                    }
                    if (row.getCell(4) != null) {
                        int numericCellValue = (int) (row.getCell(4).getNumericCellValue());
                        bloodPressureReading.setDiastole(numericCellValue);
                    }
                    if (row.getCell(5) != null) {
                        int numericCellValue = (int) (row.getCell(5).getNumericCellValue());
                        bloodPressureReading.setHeartRate(numericCellValue);
                    }

                    if (bloodPressureReading.hasData()) {
                        bloodPressureReading.setReadingTime(LocalDateTime.of(localDate, localTime));
                        newBloodPressureReadings.add(bloodPressureReading);
                    }
                }
            });
        } catch (EncryptedDocumentException | IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        List<LocalDate> dates = newBloodPressureReadings.stream().map(BloodPressureReading::getReadingTime).map(LocalDateTime::toLocalDate).distinct().toList();
        List<UserModel> userModelList = Collections.singletonList(userModel);
        importContext.ensureDailyEvaluations(dates, userModelList, evaluationDataService);

        newBloodPressureReadings.forEach(dose -> {
            dose.setDailyEvaluation(importContext.getDailyEvaluation(dose.getReadingTime().toLocalDate(), userModel));
        });

        patientsService.saveBloodPressureReadings(newBloodPressureReadings);
    }
}
