package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.enums.DAYSTAGE;
import com.cathalob.medtracker.model.enums.USERROLE;
import com.cathalob.medtracker.model.tracking.BloodPressureReading;
import com.cathalob.medtracker.model.tracking.DailyEvaluation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BloodPressureReadingRepositoryTests {
    @Autowired
    private BloodPressureReadingRepository bloodPressureReadingRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test

    public void givenBloodPressureReading_whenSaved_thenReturnSavedBloodPressureReading() {
//    given
        UserModel userModel = new UserModel();
        userModel.setUsername("name");
        userModel.setPassword("abc");
        userModel.setRole(USERROLE.USER);
        testEntityManager.persist(userModel);

        DailyEvaluation dailyEvaluation = new DailyEvaluation();
        dailyEvaluation.setUserModel(userModel);
        dailyEvaluation.setRecordDate(LocalDate.now());
        testEntityManager.persist(dailyEvaluation);

        BloodPressureReading bloodPressureReading = new BloodPressureReading();
        bloodPressureReading.setReadingTime(LocalDateTime.now());
        bloodPressureReading.setDiastole(80);
        bloodPressureReading.setSystole(120);
        bloodPressureReading.setHeartRate(60);
        bloodPressureReading.setDayStage(DAYSTAGE.MIDDAY);
        bloodPressureReading.setDailyEvaluation(dailyEvaluation);

//    when
        BloodPressureReading saved = bloodPressureReadingRepository.save(bloodPressureReading);

//    then
        assertThat(saved.getId()).isEqualTo(1);
    }
}