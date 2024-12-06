package com.cathalob.medtracker.model.prescription;
import com.cathalob.medtracker.model.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDateTime;

@Data
@Entity(name = "PRESCRIPTION")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int doseMg;

    @OneToOne
    @JoinColumn
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @Cascade(CascadeType.MERGE)
    private Medication medication;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PATIENT_ID", nullable = false)
    @Cascade(CascadeType.MERGE)
    private UserModel patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRACTITIONER_ID", nullable = false)

    @Cascade(CascadeType.MERGE)
    private UserModel practitioner;


    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime beginTime;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

}
