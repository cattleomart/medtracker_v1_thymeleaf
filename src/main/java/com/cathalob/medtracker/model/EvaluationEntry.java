package com.cathalob.medtracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.util.Date;
//,
//    foreign key (userModel_id) references userModel(userModel_id)
@Entity(name ="EVALUATIONENTRY")
@Data
public class EvaluationEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usermodel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel userModel;
    private Date recordDate;
    private String medication;
    private int dose1;
    private int dose2;

    private  Integer bloodPressureSystole;
    private Integer bloodPressureDiastole;
    private Integer heartRate;

    private Integer lunchBloodPressureSystole;
    private Integer lunchBloodPressureDiastole;
    private Integer lunchHeartRate;


//    SDP prefix means Second Dose Peak, the mid-point of the second daily dose.
    private Integer SDPBloodPressureSystole;
    private Integer SDPBloodPressureDiastole;
    private Integer SDPHeartRate;


    public static Integer BpSystoleUpperBound = 140;
    public static Integer BpSystoleLowerBound = 50;
    public static Integer BpDiastoleUpperBound = 90;
    public  static Integer BpDiastoleLowerBound = 60;


    public EvaluationEntry(String newDatePrefix, Integer index) {
        recordDate = new Date();
        bloodPressureSystole = 105 + (index * 2);
        bloodPressureDiastole = 70 + (index * 2);
        heartRate = 60 + (index * 2);
    }
    public EvaluationEntry() {
    }


    public boolean hasData(){

        boolean morningReading = bloodPressureDiastole != null && bloodPressureSystole != null && heartRate != null;
        boolean lunchReading = lunchBloodPressureDiastole != null && lunchBloodPressureSystole != null && lunchHeartRate != null;
        boolean secondPeak = SDPBloodPressureDiastole != null && SDPBloodPressureSystole != null && SDPHeartRate != null;
        return morningReading || lunchReading || secondPeak;

    }
//    public boolean isComplete(){
//
//        return hasData() && true
//
//    }

}
