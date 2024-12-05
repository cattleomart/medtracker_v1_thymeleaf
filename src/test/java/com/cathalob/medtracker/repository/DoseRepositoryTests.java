package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.enums.DAYSTAGE;
import com.cathalob.medtracker.model.enums.USERROLE;
import com.cathalob.medtracker.model.prescription.Medication;
import com.cathalob.medtracker.model.prescription.Prescription;
import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.cathalob.medtracker.model.tracking.DailyEvaluation;
import com.cathalob.medtracker.model.tracking.Dose;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class DoseRepositoryTests {

    @Autowired
    private DoseRepository doseRepository;
    @Autowired
    private PrescriptionsRepository prescriptionsRepository;

    @Autowired
    private PrescriptionScheduleEntryRepository prescriptionScheduleEntryRepository;
    @Autowired
    private MedicationRepository medicationRepository;

@Autowired
private DailyEvaluationRepository dailyEvaluationRepository;
    @Autowired
    private UserModelRepository userModelRepository;

    @Disabled("testing if cascade fixes need to persist so much first")
    @Test
    public void givenDose_whenSaved_thenReturnSavedDose(){
        UserModel userModel = new UserModel();
        userModel.setUsername("name");
        userModel.setPassword("abc");
        userModel.setRole(USERROLE.USER);
        UserModel savedUserModel = userModelRepository.save(userModel);

        DailyEvaluation dailyEvaluation = new DailyEvaluation();
        dailyEvaluation.setUserModel(savedUserModel);
        dailyEvaluation.setRecordDate(LocalDate.now());

        DailyEvaluation dailyEvaluation1 = dailyEvaluationRepository.save(dailyEvaluation);
        Medication medication = new Medication();
        medication.setName("med");
        Medication medication1 = medicationRepository.save(medication);

        Prescription prescription = new Prescription();
        prescription.setDoseMg(15);
        prescription.setPractitioner(savedUserModel);
        prescription.setPatient(savedUserModel);
        prescription.setBeginTime(LocalDateTime.now());
        prescription.setMedication(medication1);
        Prescription prescription1 = prescriptionsRepository.save(prescription);

        PrescriptionScheduleEntry prescriptionScheduleEntry = new PrescriptionScheduleEntry();
        prescriptionScheduleEntry.setPrescription(prescription1);
        prescriptionScheduleEntry.setDayStage(DAYSTAGE.MIDDAY);
        PrescriptionScheduleEntry prescriptionScheduleEntry1 = prescriptionScheduleEntryRepository.save(prescriptionScheduleEntry);


        Dose dose = new Dose();
        dose.setDoseTime(LocalDateTime.now());
        dose.setTaken(true);
        dose.setEvaluation(dailyEvaluation1);
        dose.setPrescriptionScheduleEntry(prescriptionScheduleEntry1);


        Dose saved = doseRepository.save(dose);
        assertThat(saved.getId()).isGreaterThan(0);

    }

    @Test
    public void givenDose_whenSaved_thenReturnSavedDose_cascade(){
        UserModel userModel = new UserModel();
        userModel.setUsername("name");
        userModel.setPassword("abc");
        userModel.setRole(USERROLE.USER);

//        UserModel savedUserModel = userModelRepository.save(userModel);


        DailyEvaluation dailyEvaluation = new DailyEvaluation();
        dailyEvaluation.setUserModel(userModel);
        dailyEvaluation.setRecordDate(LocalDate.now());


        Medication medication = new Medication();
        medication.setName("med");


        Prescription prescription = new Prescription();
        prescription.setDoseMg(15);
        prescription.setPractitioner(userModel);
        prescription.setPatient(userModel);
        prescription.setBeginTime(LocalDateTime.now());
        prescription.setMedication(medication);


        PrescriptionScheduleEntry prescriptionScheduleEntry = new PrescriptionScheduleEntry();
        prescriptionScheduleEntry.setPrescription(prescription);
        prescriptionScheduleEntry.setDayStage(DAYSTAGE.MIDDAY);


        Dose dose = new Dose();
        dose.setDoseTime(LocalDateTime.now());
        dose.setTaken(true);
        dose.setEvaluation(dailyEvaluation);
        dose.setPrescriptionScheduleEntry(prescriptionScheduleEntry);


        Dose saved = doseRepository.save(dose);
        assertThat(saved.getId()).isGreaterThan(0);

    }

}