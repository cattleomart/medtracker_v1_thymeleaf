package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.enums.USERROLE;
import com.cathalob.medtracker.model.tracking.DailyEvaluation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DailyEvaluationRepositoryTests {
    @Autowired
    private DailyEvaluationRepository dailyEvaluationRepository;

    @Test
    public void giveDailyEvaluation_whenSaved_thenGetSavedDailyEvaluation(){
//        given

        UserModel patient = new UserModel();
        patient.setUsername("name");
        patient.setPassword("abc");
        patient.setRole(USERROLE.USER);

        DailyEvaluation dailyEvaluation = new DailyEvaluation();
        dailyEvaluation.setUserModel(patient);
        dailyEvaluation.setRecordDate(LocalDate.now());

//        when

        dailyEvaluationRepository.save(dailyEvaluation);

//        when

        boolean present = dailyEvaluationRepository.findAll().stream()
                .anyMatch(d -> d.getDailyEvaluationIdClass().equals(dailyEvaluation.getDailyEvaluationIdClass()));
        assertThat(present).isTrue();
    }

}