package com.cathalob.medtracker.fileupload;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.cathalob.medtracker.model.tracking.Dose;
import com.cathalob.medtracker.service.EvaluationDataService;
import com.cathalob.medtracker.service.PrescriptionsService;
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
public class DoseFileImporter {
    private final ImportContext importContext;
    private final EvaluationDataService evaluationDataService;
    private final PrescriptionsService prescriptionsService;
    private UserModel userModel;

    public DoseFileImporter(UserModel userModel, EvaluationDataService evaluationDataService, PrescriptionsService prescriptionsService) {
        this.evaluationDataService = evaluationDataService;
        this.prescriptionsService = prescriptionsService;
        importContext = new ImportContext();
        importContext.loadPrescriptions(prescriptionsService);
        importContext.loadPrescriptionScheduleEntries(prescriptionsService);
        importContext.loadDailyEvaluations(evaluationDataService);
        importContext.loadDoses(prescriptionsService);
        this.userModel = userModel;
    }

    public void processFile(MultipartFile fileToImport) {
        List<Dose> newDoses = new ArrayList<>();
        log.info(this.getClass().toString() + " User: " + userModel.getUsername() + " FN: " + fileToImport.getOriginalFilename());
        XSSFWorkbook workbook = null;

        try {
            
            workbook = new XSSFWorkbook(fileToImport.getInputStream());
//            log.info("Number of sheets: " + workbook.getNumberOfSheets());

            workbook.forEach(sheet -> {
//                log.info("Title of sheet => " + sheet.getSheetName());


                int index = 0;
                for (Row row : sheet) {
                    if (index++ == 0) continue;
                    Dose dose = new Dose();
                    LocalDate localDate = LocalDate.now();
                    LocalTime localTime = LocalTime.now();

                    if (row.getCell(0) != null) {
                        localDate = LocalDate.ofInstant(
                                row.getCell(0).getDateCellValue().toInstant(), ZoneId.systemDefault());
                    }
                    if (row.getCell(1) != null) {
                        int numericCellValue = (int) row.getCell(1).getNumericCellValue();
                        PrescriptionScheduleEntry prescriptionScheduleEntry = importContext.getPrescriptionScheduleEntry(numericCellValue);
                        dose.setPrescriptionScheduleEntry(prescriptionScheduleEntry);
                    }

                    if (row.getCell(2) != null) {
                        boolean booleanCellValue = row.getCell(2).getBooleanCellValue();
                        dose.setTaken(booleanCellValue);
                    }
                    if (row.getCell(3) != null) {
                        localTime = (row.getCell(3).getLocalDateTimeCellValue().toLocalTime());
                    }
                    dose.setDoseTime(LocalDateTime.of(localDate,localTime));
                    newDoses.add(dose);
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
        List<LocalDate> dates = newDoses.stream().map(Dose::getDoseTime).map(LocalDateTime::toLocalDate).distinct().toList();
        List<UserModel> userModelList = Collections.singletonList(userModel);
        importContext.ensureDailyEvaluations(dates, userModelList, evaluationDataService);

        newDoses.forEach(dose -> {
            dose.setEvaluation(importContext.getDailyEvaluation(dose.getDoseTime().toLocalDate(), userModel));
        });

        prescriptionsService.saveDoses(newDoses);
        newDoses.forEach(dose -> {
            importContext.getDoses().put(dose.getId(), dose);
        });
    }
}
