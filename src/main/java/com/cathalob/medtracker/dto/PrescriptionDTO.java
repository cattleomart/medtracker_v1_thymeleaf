package com.cathalob.medtracker.dto;

import com.cathalob.medtracker.model.prescription.DAYSTAGE;
import com.cathalob.medtracker.model.prescription.Drug;
import lombok.Data;

@Data
public class PrescriptionDTO {
    private Drug drug;
    private int doseMg;
    private DAYSTAGE daystage;
}
