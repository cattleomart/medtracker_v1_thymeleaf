package com.cathalob.medtracker.initialdata;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.enums.DAYSTAGE;
import com.cathalob.medtracker.model.prescription.Medication;
import com.cathalob.medtracker.model.prescription.Prescription;
import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.cathalob.medtracker.model.tracking.BloodPressureReading;
import com.cathalob.medtracker.model.tracking.DailyEvaluation;
import com.cathalob.medtracker.model.tracking.DailyEvaluationId;
import com.cathalob.medtracker.model.tracking.Dose;
import com.cathalob.medtracker.service.EvaluationDataService;
import com.cathalob.medtracker.service.PatientsService;
import com.cathalob.medtracker.service.PrescriptionsService;
import com.cathalob.medtracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InitialDataLoader implements ApplicationRunner {
    private Map<Integer, UserModel> userModels;
    private Map<Integer, Medication> medications;
    private Map<Integer, Prescription> prescriptions;
    private Map<Integer, PrescriptionScheduleEntry> prescriptionScheduleEntries;
    private Map<Integer, Dose> doses;
    private Map<DailyEvaluationId, DailyEvaluation> dailyEvaluations;

    public InitialDataLoader() {
        this.medications = new HashMap<>();
        this.prescriptions = new HashMap<>();
        this.userModels = new HashMap<>();
        this.prescriptionScheduleEntries = new HashMap<>();
        this.doses = new HashMap<>();
        this.dailyEvaluations = new HashMap<>();
    }

    @Autowired
    EvaluationDataService evaluationDataService;
    @Autowired
    PatientsService patientsService;
    @Autowired
    PrescriptionsService prescriptionsService;
    @Autowired
    UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        preLoadDbData();
//        basic data from data.sql only contains one medication, if more than one is present then we should not load from file again
        if (medications.containsKey(2)) return;
        loadDbData();
        processMedicationExcelFile();
        processPrescriptionExcelFile();
        processPrescriptionScheduleEntriesExcelFile();
        processDoseExcelFile();
        processBloodPressureReadingsExcelFile();
    }

    private void loadDbData() {
        prescriptions = prescriptionsService.getPrescriptionsById();
        userModels = userService.getUserModelsById();
        dailyEvaluations = evaluationDataService.getDailyEvaluationsById();

    }

    private void preLoadDbData() {
//        load data to check if we have loaded data from file already after a fresh db creation.
        medications = prescriptionsService.getMedications().stream().collect(Collectors.toMap(Medication::getId, Function.identity()));

    }

    public void processMedicationExcelFile() {
        List<Medication> newMedications = new ArrayList<>();
        Workbook workbook = null;

        try {

            FileInputStream fileInputStream = new FileInputStream("./src/main/resources/initialDataFiles/medications.xlsx");
            workbook = new XSSFWorkbook(fileInputStream);
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
//                    log.info(medication.toString());
                    newMedications.add(medication);
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
        prescriptionsService.saveMedications(newMedications);
        newMedications.forEach(newMedication -> medications.put(newMedication.getId(), newMedication));
    }

    public void processPrescriptionExcelFile() {
        List<Prescription> newPrescriptions = new ArrayList<>();
        XSSFWorkbook workbook = null;

        try {
            FileInputStream fileInputStream = new FileInputStream("./src/main/resources/initialDataFiles/prescriptions.xlsx");
            workbook = new XSSFWorkbook(fileInputStream);
//            log.info("Number of sheets: " + workbook.getNumberOfSheets());

            workbook.forEach(sheet -> {
//                log.info("Title of sheet => " + sheet.getSheetName());
                int index = 0;
                for (Row row : sheet) {
                    if (index++ == 0) continue;
                    Prescription prescription = new Prescription();
                    if (row.getCell(1) != null) {
                        int numericCellValue = (int) row.getCell(1).getNumericCellValue();
                        Medication medication = medications.get(numericCellValue);
//                        log.info(String.valueOf(numericCellValue));
//                        log.info(medications.toString());
//                        log.info("Medication for prescription: " + index + " + " + medication);
                        prescription.setMedication(medication);
                    }
                    if (row.getCell(2) != null) {
                        int numericCellValue = (int) row.getCell(2).getNumericCellValue();
                        UserModel userModel = userModels.get(numericCellValue);
                        prescription.setPatient(userModel);
                    }
                    if (row.getCell(3) != null) {
                        int numericCellValue = (int) row.getCell(3).getNumericCellValue();
                        UserModel userModel = userModels.get(numericCellValue);
                        prescription.setPractitioner(userModel);
                    }

                    if (row.getCell(4) != null) {
                        LocalDateTime localDateTimeCellValue = row.getCell(4).getLocalDateTimeCellValue();
                        prescription.setBeginTime(localDateTimeCellValue);
                    }
                    if (row.getCell(5) != null) {
                        LocalDateTime localDateTimeCellValue = row.getCell(4).getLocalDateTimeCellValue();

                        prescription.setEndTime(localDateTimeCellValue);
                    }


                    newPrescriptions.add(prescription);
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
        prescriptionsService.savePrescriptions(newPrescriptions);
        newPrescriptions.forEach(prescription -> prescriptions.put(prescription.getId(), prescription));
    }

    public void processPrescriptionScheduleEntriesExcelFile() {
        List<PrescriptionScheduleEntry> newPrescriptionScheduleEntries = new ArrayList<>();

        XSSFWorkbook workbook = null;

        try {
            FileInputStream fileInputStream = new FileInputStream("./src/main/resources/initialDataFiles/prescriptionScheduleEntries.xlsx");
            workbook = new XSSFWorkbook(fileInputStream);
//            log.info("Number of sheets: " + workbook.getNumberOfSheets());

            workbook.forEach(sheet -> {
//                log.info("Title of sheet => " + sheet.getSheetName());

                int index = 0;
                for (Row row : sheet) {
                    if (index++ == 0) continue;
                    PrescriptionScheduleEntry prescriptionScheduleEntry = new PrescriptionScheduleEntry();
                    if (row.getCell(0) != null) {
                        int numericCellValue = (int) row.getCell(0).getNumericCellValue();
                        Prescription prescription = prescriptions.get(numericCellValue);
//                        log.info(String.valueOf(numericCellValue));
//                        log.info(medications.toString());
//                        log.info("Medication for prescription: " + index + " + " + medication);
                        prescriptionScheduleEntry.setPrescription(prescription);
                    }
                    if (row.getCell(1) != null) {
                        String daystage = row.getCell(1).getStringCellValue();
                        prescriptionScheduleEntry.setDayStage(DAYSTAGE.valueOf(daystage));
                    }
                    newPrescriptionScheduleEntries.add(prescriptionScheduleEntry);
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
        prescriptionsService.savePrescriptionScheduleEntries(newPrescriptionScheduleEntries);
        newPrescriptionScheduleEntries.forEach(prescriptionScheduleEntry -> prescriptionScheduleEntries.put(prescriptionScheduleEntry.getId(), prescriptionScheduleEntry));
    }

    public void processDoseExcelFile() {
        List<Dose> newDoses = new ArrayList<>();

        XSSFWorkbook workbook = null;

        try {
            FileInputStream fileInputStream = new FileInputStream("./src/main/resources/initialDataFiles/doses.xlsx");
            workbook = new XSSFWorkbook(fileInputStream);
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
                        PrescriptionScheduleEntry prescriptionScheduleEntry = prescriptionScheduleEntries.get(numericCellValue);
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
        List<UserModel> userModelList = Arrays.asList(userModels.get(3));
        ensureDailyEvaluations(dates, userModelList);

        newDoses.forEach(dose -> {
            dose.setEvaluation(dailyEvaluations.get(this.getDailyEvaluationKey(dose.getDoseTime().toLocalDate(), userModels.get(3))));
        });

        prescriptionsService.saveDoses(newDoses);

        newDoses.forEach(dose -> {
            doses.put(dose.getId(), dose);
        });
    }

    public void processBloodPressureReadingsExcelFile() {
        List<BloodPressureReading> newBloodPressureReadings = new ArrayList<>();

        XSSFWorkbook workbook = null;

        try {
            FileInputStream fileInputStream = new FileInputStream("./src/main/resources/initialDataFiles/bloodPressureReadings.xlsx");
            workbook = new XSSFWorkbook(fileInputStream);
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
                        int numericCellValue = (int) row.getCell(1).getNumericCellValue();
                        PrescriptionScheduleEntry prescriptionScheduleEntry = prescriptionScheduleEntries.get(numericCellValue);
                        bloodPressureReading.setPrescriptionScheduleEntry(prescriptionScheduleEntry);
                    }

                    if (row.getCell(2) != null) {
                        int numericCellValue = (int) (row.getCell(2).getNumericCellValue());
                        bloodPressureReading.setSystole(numericCellValue);
                    }
                    if (row.getCell(3) != null) {
                        int numericCellValue = (int) (row.getCell(3).getNumericCellValue());
                        bloodPressureReading.setDiastole(numericCellValue);
                    }
                    if (row.getCell(4) != null) {
                        int numericCellValue = (int) (row.getCell(4).getNumericCellValue());
                        bloodPressureReading.setHeartRate(numericCellValue);
                    }
                    if (row.getCell(5) != null) {
                        localTime = (row.getCell(5).getLocalDateTimeCellValue().toLocalTime());
                    }
                    bloodPressureReading.setReadingTime(LocalDateTime.of(localDate,localTime));
                    newBloodPressureReadings.add(bloodPressureReading);
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
        List<UserModel> userModelList = Arrays.asList(userModels.get(3));
        ensureDailyEvaluations(dates, userModelList);

        newBloodPressureReadings.forEach(dose -> {
            dose.setDailyEvaluation(dailyEvaluations.get(this.getDailyEvaluationKey(dose.getReadingTime().toLocalDate(), userModels.get(3))));
        });

        patientsService.saveBloodPressureReadings(newBloodPressureReadings);
    }

    private void ensureDailyEvaluations(List<LocalDate> dates, List<UserModel> userModels) {
        dates.forEach(localDate -> {
            userModels.forEach(userModel -> {
                DailyEvaluationId dailyEvaluationKey = this.getDailyEvaluationKey(localDate, userModel);
                if (!dailyEvaluations.containsKey(dailyEvaluationKey)) {
                    DailyEvaluation dailyEvaluation = new DailyEvaluation();
                    dailyEvaluation.setRecordDate(localDate);
                    dailyEvaluation.setUserModel(userModel);
                    evaluationDataService.addDailyEvaluation(dailyEvaluation);
                    dailyEvaluations.put(dailyEvaluationKey, dailyEvaluation);
                }
            });
        });

    }

    private DailyEvaluationId getDailyEvaluationKey(LocalDate localDate, UserModel userModel) {
        return new DailyEvaluationId(userModel.getId(), localDate);
    }
}