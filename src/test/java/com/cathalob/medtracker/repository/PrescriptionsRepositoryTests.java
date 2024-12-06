package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.enums.USERROLE;
import com.cathalob.medtracker.model.prescription.Medication;
import com.cathalob.medtracker.model.prescription.Prescription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class PrescriptionsRepositoryTests {
    @Autowired
    private PrescriptionsRepository prescriptionsRepository;

    @Test
    public void givenPrescription_whenSaved_thenReturnSavedPrescription() {

//        given
        UserModel patient = new UserModel();
        patient.setUsername("name");
        patient.setPassword("abc");
        patient.setRole(USERROLE.USER);

        UserModel practitioner = new UserModel();
        practitioner.setUsername("name");
        practitioner.setPassword("abc");
        practitioner.setRole(USERROLE.USER);

        Medication medication = new Medication();
        medication.setName("med");


        Prescription prescription = new Prescription();
        prescription.setDoseMg(15);
        prescription.setPatient(patient);
        prescription.setPractitioner(practitioner);
        prescription.setBeginTime(LocalDateTime.now());
        prescription.setMedication(medication);

//        when
        Prescription savedPrescription = prescriptionsRepository.save(prescription);

//        then
        assertThat(savedPrescription.getId()).isGreaterThan(0);
        assertThat(savedPrescription.getMedication().getId()).isGreaterThan(0);
        assertThat(savedPrescription.getPatient().getId()).isGreaterThan(0);
        assertThat(savedPrescription.getPractitioner().getId()).isGreaterThan(0);

    }
}