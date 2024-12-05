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

    @Test
    public void givenDose_whenSaved_thenReturnSavedDose_cascade() {
        //        given
        UserModel patient = new UserModel();
        patient.setUsername("name");
        patient.setPassword("abc");
        patient.setRole(USERROLE.USER);

        UserModel practitioner = new UserModel();
        practitioner.setUsername("name");
        practitioner.setPassword("abc");
        practitioner.setRole(USERROLE.USER);

        DailyEvaluation dailyEvaluation = new DailyEvaluation();
        dailyEvaluation.setUserModel(patient);
        dailyEvaluation.setRecordDate(LocalDate.now());

        Medication medication = new Medication();
        medication.setName("med");


        Prescription prescription = new Prescription();
        prescription.setDoseMg(15);
        prescription.setPatient(patient);
        prescription.setPractitioner(practitioner);
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

//      when
        Dose saved = doseRepository.save(dose);

//      then
        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getPrescriptionScheduleEntry().getPrescription().getMedication().getId()).isGreaterThan(0);
        assertThat(saved.getPrescriptionScheduleEntry().getPrescription().getPatient().getId()).isGreaterThan(0);
        assertThat(saved.getPrescriptionScheduleEntry().getPrescription().getPractitioner().getId()).isGreaterThan(0);
        assertThat(saved.getEvaluation().getUserModel().getId()).isGreaterThan(0);
        assertThat(saved.isTaken()).isTrue();

    }

}