package com.cathalob.medtracker.model.prescription;

import com.cathalob.medtracker.model.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Time;

@Data
@Entity(name = "PRESCRIPTION")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int doseMg;
    @Enumerated(EnumType.STRING)
    private Drug drug;

    @OneToOne
    @JoinColumn
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Medication medication;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PATIENT_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel patient;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRACTITIONER_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel practitioner;


    @Temporal(TemporalType.TIME)
    private Time beginTime;
    @Temporal(TemporalType.TIME)
    private Time endTime;

}
