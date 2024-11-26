package com.cathalob.medtracker.model.tracking;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class DailyEvaluationId implements Serializable {
    private int dailyEvaluationUserModelId;
    private Date dailyEvaluationDate;

    public DailyEvaluationId(int dailyEvaluationId, Date dailyEvaluationDate) {
        this.dailyEvaluationUserModelId = dailyEvaluationId;
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
