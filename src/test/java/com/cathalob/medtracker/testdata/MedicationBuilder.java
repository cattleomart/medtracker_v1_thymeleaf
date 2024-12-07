package com.cathalob.medtracker.testdata;

import com.cathalob.medtracker.model.prescription.Medication;

public class MedicationBuilder {

    private Integer id;
    private String name = "Medication";


    public MedicationBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public MedicationBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public static MedicationBuilder aMedication(){
        return new MedicationBuilder();

    }
    public Medication build(){
        return new Medication(null, name);
    }


}
