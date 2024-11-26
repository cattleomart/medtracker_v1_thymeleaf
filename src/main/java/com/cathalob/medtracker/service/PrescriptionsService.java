package com.cathalob.medtracker.service;

import com.cathalob.medtracker.model.prescription.Medication;
import com.cathalob.medtracker.repository.MedicationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class PrescriptionsService {

    @Autowired
    private final MedicationRepository medicationRepository;

    public void saveMedications(List<Medication> medicationList){
        medicationRepository.saveAll(medicationList);

    }
    public List<Medication> getMedications(){
        return medicationRepository.findAll();

    }
}
