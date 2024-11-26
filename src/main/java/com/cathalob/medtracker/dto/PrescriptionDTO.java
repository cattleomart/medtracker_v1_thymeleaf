package com.cathalob.medtracker.dto;

import com.cathalob.medtracker.model.enums.DAYSTAGE;
import com.cathalob.medtracker.model.prescription.Drug;
import lombok.Data;

@Data
public class PrescriptionDTO {
    private Drug drug;
    private int doseMg;
    private DAYSTAGE daystage;
}
