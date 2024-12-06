package com.cathalob.medtracker.model.tracking;
import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.sql.Time;
import java.time.LocalDateTime;

@Entity(name = "DOSE")

@Data
public class Dose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "DAILYEVALUATION_RECORD_DATE")
    @JoinColumn(name = "DAILYEVALUATION_USERMODEL_ID")
    @Cascade(CascadeType.MERGE)
    private DailyEvaluation evaluation;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime doseTime;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRESCRIPTION_SCHEDULE_ENTRY_ID", nullable = false)
    @Cascade(CascadeType.MERGE)
    private PrescriptionScheduleEntry prescriptionScheduleEntry;

    private boolean taken;
}
