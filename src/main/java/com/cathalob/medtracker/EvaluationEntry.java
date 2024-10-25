package com.cathalob.medtracker;

public class EvaluationEntry {
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBloodPressureSystole(Integer bloodPressureSystole) {
        this.bloodPressureSystole = bloodPressureSystole;
    }

    public Integer getBloodPressureSystole() {
        return bloodPressureSystole;
    }

    private String date;
    private  Integer bloodPressureSystole;

    public EvaluationEntry(String newDatePrefix, Integer index) {
        date = newDatePrefix + index;
        bloodPressureSystole = index;
    }

    public EvaluationEntry() {
    }
}
