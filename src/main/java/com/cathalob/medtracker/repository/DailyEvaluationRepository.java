package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.tracking.DailyEvaluation;
import com.cathalob.medtracker.model.tracking.DailyEvaluationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyEvaluationRepository extends JpaRepository<DailyEvaluation, DailyEvaluationId> {
}
