package com.cathalob.medtracker;

public class EvaluationEntry {
    private String date;
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    private  Integer bloodPressureSystole;
    public void setBloodPressureSystole(Integer bloodPressureSystole) {
        this.bloodPressureSystole = bloodPressureSystole;
    }
    public Integer getBloodPressureSystole() {
        return bloodPressureSystole;
    }

    private Integer bloodPressureDiastole;
    private Integer heartRate;

    public Integer getBloodPressureDiastole() {
        return bloodPressureDiastole;
    }

    public void setBloodPressureDiastole(Integer bloodPressureDiastole) {
        this.bloodPressureDiastole = bloodPressureDiastole;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public static Integer BpSystoleUpperBound = 140;
    public static Integer BpSystoleLowerBound = 90;
    public static Integer BpDiastoleUpperBound = 80;
    public  static Integer BpDiastoleLowerBound = 60;
    public EvaluationEntry(String newDatePrefix, Integer index) {
        date = newDatePrefix + "0" + index;
        bloodPressureSystole = 105 + (index * 2);
        bloodPressureDiastole = 70 + (index * 2);
        heartRate = 60 + (index * 2);
    }
    public EvaluationEntry() {
    }


}
