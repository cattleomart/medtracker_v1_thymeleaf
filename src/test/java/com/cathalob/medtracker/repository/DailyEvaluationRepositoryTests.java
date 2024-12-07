package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.tracking.DailyEvaluation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.cathalob.medtracker.testdata.DailyEvaluationBuilder.aDailyEvaluation;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;


@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class DailyEvaluationRepositoryTests {
    @Autowired
    private DailyEvaluationRepository dailyEvaluationRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void giveDailyEvaluation_whenSaved_thenGetSavedDailyEvaluation() {
//        given
        DailyEvaluation dailyEvaluation = aDailyEvaluation().build();
        testEntityManager.persist(dailyEvaluation.getUserModel());
        dailyEvaluation.setUserModel(dailyEvaluation.getUserModel());
        dailyEvaluation.setRecordDate(LocalDate.now());

//        when
        dailyEvaluationRepository.save(dailyEvaluation);

//        when
        boolean present = dailyEvaluationRepository.findAll().stream()
                .anyMatch(d -> d.getDailyEvaluationIdClass().equals(dailyEvaluation.getDailyEvaluationIdClass()));
        assertThat(present).isTrue();
    }

}