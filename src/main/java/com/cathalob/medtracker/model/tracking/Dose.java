package com.cathalob.medtracker.model.tracking;
import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.sql.Time;

@Entity(name = "DOSE")

@Data
public class Dose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "EVALUATION_ID")
    @JoinColumn(name = "RECORD_DATE")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private DailyEvaluation evaluation;

    @Temporal(TemporalType.TIME)
    private Time doseTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRESCRIPTIONSCHEDULEENTRY_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private PrescriptionScheduleEntry prescriptionScheduleEntry;

    private boolean taken;
}
