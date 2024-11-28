package com.cathalob.medtracker.model.tracking;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class DailyEvaluationId implements Serializable {
    private int dailyEvaluationUserModelId;
    private LocalDate dailyEvaluationDate;

    public DailyEvaluationId(int dailyEvaluationUserModelId, LocalDate dailyEvaluationDate) {
        this.dailyEvaluationUserModelId = dailyEvaluationUserModelId;
        this.dailyEvaluationDate = dailyEvaluationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyEvaluationId that = (DailyEvaluationId) o;
        return dailyEvaluationUserModelId == that.dailyEvaluationUserModelId && Objects.equals(dailyEvaluationDate, that.dailyEvaluationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dailyEvaluationUserModelId, dailyEvaluationDate);
    }
}
