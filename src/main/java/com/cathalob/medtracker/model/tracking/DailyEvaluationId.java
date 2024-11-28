package com.cathalob.medtracker.model.tracking;

import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
@NoArgsConstructor
public class DailyEvaluationId implements Serializable {
    private int userModel;
    private LocalDate recordDate;

    public DailyEvaluationId(int dailyEvaluationUserModelId, LocalDate dailyEvaluationDate) {
        this.userModel = dailyEvaluationUserModelId;
        this.recordDate = dailyEvaluationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyEvaluationId that = (DailyEvaluationId) o;
        return userModel == that.userModel && Objects.equals(recordDate, that.recordDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userModel, recordDate);
    }
}
