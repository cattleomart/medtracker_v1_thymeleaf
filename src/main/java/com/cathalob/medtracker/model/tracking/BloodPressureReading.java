package com.cathalob.medtracker.model.tracking;

import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;



@Entity(name = "BLOODPRESSUREREADING")
@Data
public class BloodPressureReading {
    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRESCRIPTION_SCHEDULE_ENTRY_ID", nullable = false)
    @JsonIgnore
    private PrescriptionScheduleEntry prescriptionScheduleEntry;

    @ManyToOne
    @JoinColumn(name = "DAILYEVALUATION_RECORD_DATE")
    @JoinColumn(name = "DAILYEVALUATION_USERMODEL_ID")
    @JsonIgnore
    private DailyEvaluation dailyEvaluation;

    private Integer systole;
    private Integer diastole;
    private Integer heartRate;

}