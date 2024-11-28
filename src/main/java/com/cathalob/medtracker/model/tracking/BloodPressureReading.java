package com.cathalob.medtracker.model.tracking;

import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity(name = "BLOODPRESSUREREADING")
@Data
public class BloodPressureReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime readingTime;

    private Integer systole;
    private Integer diastole;
    private Integer heartRate;

}