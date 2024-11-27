package com.cathalob.medtracker.service;

import com.cathalob.medtracker.model.prescription.Medication;
import com.cathalob.medtracker.model.prescription.Prescription;
import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.cathalob.medtracker.repository.MedicationRepository;
import com.cathalob.medtracker.repository.PrescriptionScheduleEntryRepository;
import com.cathalob.medtracker.repository.PrescriptionsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PrescriptionsService {

    @Autowired
    private final MedicationRepository medicationRepository;

    @Autowired
    private final PrescriptionScheduleEntryRepository prescriptionScheduleEntryRepository;
    @Autowired
    private final PrescriptionsRepository prescriptionsRepository;

    public void saveMedications(List<Medication> medicationList){
        medicationRepository.saveAll(medicationList);

    }
    public List<Medication> getMedications(){
        return medicationRepository.findAll();
    }

    public List<Prescription> getPrescriptions(){
        return prescriptionsRepository.findAll();
    }

    public Map<Integer, List<PrescriptionScheduleEntry>> getPrescriptionScheduleEntriesByPrescriptionId(){
        return prescriptionScheduleEntryRepository.findAll()
                .stream().collect(Collectors.groupingBy(prescriptionScheduleEntry -> prescriptionScheduleEntry.getPrescription().getId()));
    }

    public Map<Integer, List<Medication>> getMedicationById(){
        return medicationRepository.findAll()
                .stream().collect(Collectors.groupingBy(Medication::getId));
    }


    public void savePrescriptions(List<Prescription> newPrescriptions) {
        prescriptionsRepository.saveAll(newPrescriptions);
    }
}
