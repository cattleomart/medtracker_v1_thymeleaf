package com.cathalob.medtracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;
//,
//    foreign key (userModel_id) references userModel(userModel_id)
@Table("EVALUATIONENTRY")
public class EvaluationEntry {
    @Id
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usermodel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel userModel = new UserModel();
    private Date recordDate;
    public Date getRecordDate() {
        return recordDate;
    }
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
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
        recordDate = new Date();
        bloodPressureSystole = 105 + (index * 2);
        bloodPressureDiastole = 70 + (index * 2);
        heartRate = 60 + (index * 2);
    }
    public EvaluationEntry() {
    }


}
