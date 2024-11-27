package com.cathalob.medtracker.initialdata;

import com.cathalob.medtracker.model.prescription.Medication;
import com.cathalob.medtracker.service.PrescriptionsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MedicationDataLoader implements ApplicationRunner {

    @Autowired
    PrescriptionsService practitionerService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (practitionerService.getMedications().size() > 1) return;
        practitionerService.saveMedications(readExcelFile());

    }

    public List<Medication> readExcelFile() {
        List<Medication> medications = new ArrayList<>();
        Workbook workbook = null;

        try {
            workbook = WorkbookFactory.create(new File("./src/main/resources/initialDataFiles/medications.xlsx"));
//            log.info("Number of sheets: " + workbook.getNumberOfSheets());

            workbook.forEach(sheet -> {
//                log.info("Title of sheet => " + sheet.getSheetName());

                DataFormatter dataFormatter = new DataFormatter();
                int index = 0;
                for (Row row : sheet) {
                    if (index++ == 0) continue;
                    Medication medication = new Medication();
                    if (row.getCell(0) != null) {
                        medication.setName(dataFormatter.formatCellValue(row.getCell(0)));
                    }
                    medications.add(medication);
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

        return medications;
    }
}
