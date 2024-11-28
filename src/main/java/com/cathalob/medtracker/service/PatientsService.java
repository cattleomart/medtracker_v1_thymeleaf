package com.cathalob.medtracker.service;

import com.cathalob.medtracker.dto.PrescriptionDTO;
import com.cathalob.medtracker.dto.PrescriptionsDTO;
import com.cathalob.medtracker.model.prescription.Medication;
import com.cathalob.medtracker.model.tracking.BloodPressureReading;
import com.cathalob.medtracker.repository.BloodPressureReadingRepository;
import com.cathalob.medtracker.repository.PrescriptionScheduleEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PatientsService {

    @Autowired
    BloodPressureReadingRepository bloodPressureReadingRepository;

    @Autowired
    PrescriptionsService prescriptionsService;

    public PrescriptionsDTO getPrescriptionsDTO(){


        Map<Integer, List<Medication>> medicationById = prescriptionsService.getMedicationById();

        PrescriptionsDTO prescriptionsDTO = new PrescriptionsDTO(new ArrayList<>());
        prescriptionsService.getPrescriptions().forEach(prescription -> {
            PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
            prescriptionDTO.setMedication(prescription.getMedication());
            prescriptionDTO.setDoseMg(prescription.getDoseMg());

            prescriptionsDTO.getPrescriptions().add(prescriptionDTO);
//        prescriptionDTO.setDaystage(prescription);

        });
        return prescriptionsDTO;
    }


    public void saveBloodPressureReadings(List<BloodPressureReading> bloodPressureReadings) {
        bloodPressureReadingRepository.saveAll(bloodPressureReadings);
    }
}
