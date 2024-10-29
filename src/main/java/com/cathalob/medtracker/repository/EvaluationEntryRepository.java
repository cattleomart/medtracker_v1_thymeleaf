package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.EvaluationEntry;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EvaluationEntryRepository extends JpaRepository<EvaluationEntry, Integer> {
}
