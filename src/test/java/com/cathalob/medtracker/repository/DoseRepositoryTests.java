package com.cathalob.medtracker.repository;


import com.cathalob.medtracker.model.prescription.Prescription;
import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.cathalob.medtracker.model.tracking.Dose;

import static com.cathalob.medtracker.testdata.DoseBuilder.aDose;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class DoseRepositoryTests {
    @Autowired
    private DoseRepository doseRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void givenDose_whenSaved_thenReturnSavedDose() {
//          given
        Dose dose = aDose().build();

        PrescriptionScheduleEntry prescriptionScheduleEntry = dose.getPrescriptionScheduleEntry();
        Prescription prescription = prescriptionScheduleEntry.getPrescription();

        testEntityManager.persist(prescription.getPatient());
        testEntityManager.persist(prescription.getPractitioner());
        testEntityManager.persist(dose.getEvaluation());
        testEntityManager.persist(prescription.getMedication());
        testEntityManager.persist(prescription);
        testEntityManager.persist(prescriptionScheduleEntry);

//      when
        Dose saved = doseRepository.save(dose);

//      then
        Prescription savedPrescription = saved.getPrescriptionScheduleEntry()
                .getPrescription();
        assertThat(saved.getId()).isEqualTo(1);
        assertThat(saved.getEvaluation().getUserModel().getId()).isEqualTo(1);
        assertThat(savedPrescription.getPractitioner().getId()).isEqualTo(2);
        assertThat(savedPrescription.getMedication().getId()).isEqualTo(1);
        assertThat(saved.getEvaluation().getUserModel().getId()).isEqualTo(savedPrescription.getPatient().getId());
    }
}