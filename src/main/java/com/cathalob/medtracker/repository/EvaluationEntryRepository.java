package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.EvaluationEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EvaluationEntryRepository extends JpaRepository<EvaluationEntry, Integer> {

    @Query("FROM EVALUATIONENTRY e WHERE e.userModel.id = :userModelId")
    List<EvaluationEntry> findEvaluationEntriesForUserId(@Param("userModelId") int userModelId);
}
