package com.cathalob.medtracker.model.prescription;

import com.cathalob.medtracker.model.enums.DAYSTAGE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Data
@Entity(name="PRESCRIPTIONSCHEDULEENTRY")
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionScheduleEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRESCRIPTION_ID", nullable = false)
    @Cascade(CascadeType.MERGE)
    private Prescription prescription;
    @Enumerated(EnumType.STRING)
    private DAYSTAGE dayStage;

}
