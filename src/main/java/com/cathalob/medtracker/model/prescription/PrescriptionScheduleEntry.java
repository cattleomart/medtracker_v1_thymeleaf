package com.cathalob.medtracker.model.prescription;

import com.cathalob.medtracker.model.enums.DAYSTAGE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Data
@Entity(name="PRESCRIPTIONSCHEDULEENTRY")
public class PrescriptionScheduleEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRESCRIPTION_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Prescription prescription;
    @Enumerated(EnumType.STRING)
    private DAYSTAGE dayStage;

}
