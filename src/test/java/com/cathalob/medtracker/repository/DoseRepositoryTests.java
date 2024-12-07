package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.UserModel;

import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.cathalob.medtracker.model.tracking.DailyEvaluation;
import com.cathalob.medtracker.model.tracking.Dose;

import static com.cathalob.medtracker.testdata.DailyEvaluationBuilder.aDailyEvaluation;
import static com.cathalob.medtracker.testdata.PrescriptionScheduleEntryBuilder.aPrescriptionScheduleEntry;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;


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
    public void givenDose_whenSaved_thenReturnSavedDose_cascade() {
//          given
        PrescriptionScheduleEntry prescriptionScheduleEntry = aPrescriptionScheduleEntry().build();
        UserModel patient = prescriptionScheduleEntry.getPrescription().getPatient();
        testEntityManager.persist(patient);
        testEntityManager.persist(prescriptionScheduleEntry.getPrescription().getPractitioner());

        DailyEvaluation dailyEvaluation = aDailyEvaluation().build();
        dailyEvaluation.setUserModel(patient);

        testEntityManager.persist(dailyEvaluation);
        testEntityManager.persist(prescriptionScheduleEntry.getPrescription().getMedication());
        testEntityManager.persist(prescriptionScheduleEntry.getPrescription());

        testEntityManager.persist(prescriptionScheduleEntry);

        Dose dose = new Dose();
        dose.setDoseTime(LocalDateTime.now());
        dose.setTaken(true);
        dose.setEvaluation(dailyEvaluation);
        dose.setPrescriptionScheduleEntry(prescriptionScheduleEntry);

//      when
        Dose saved = doseRepository.save(dose);

//      then
        assertThat(saved.getId()).isEqualTo(1);
    }
}