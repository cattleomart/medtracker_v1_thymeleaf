package com.cathalob.medtracker;

public class EvaluationEntry {
    public String getDate() {
        return date;
    }

    public Integer getRecordedValue() {
        return recordedValue;
    }

    private String date;
    private  Integer recordedValue;

    public EvaluationEntry(String newDatePrefix, Integer index) {
        date = newDatePrefix + index;
        recordedValue = index;
    }
}
