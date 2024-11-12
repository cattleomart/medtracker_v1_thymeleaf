package com.cathalob.medtracker.service;

import com.cathalob.medtracker.dto.PrescriptionDTO;
import com.cathalob.medtracker.dto.PrescriptionsDTO;
import com.cathalob.medtracker.model.prescription.Prescription;
import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.cathalob.medtracker.repository.PrescriptionScheduleEntryRepository;
import com.cathalob.medtracker.repository.PrescriptionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class PatientsService {


    @Autowired
    PrescriptionsRepository prescriptionsRepository;


    @Autowired
    PrescriptionScheduleEntryRepository prescriptionScheduleEntryRepository;

    public PrescriptionsDTO getPrescriptionsDTO(){

        Map<Integer, List<PrescriptionScheduleEntry>> grouped = prescriptionScheduleEntryRepository.findAll().stream().collect(Collectors.groupingBy(prescriptionScheduleEntry -> prescriptionScheduleEntry.getPrescription().getId()));

        PrescriptionsDTO prescriptionsDTO = new PrescriptionsDTO(new ArrayList<>());
        prescriptionsRepository.findAll().forEach(prescription -> {
            PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
            prescriptionDTO.setDrug(prescription.getDrug());
            prescriptionDTO.setDoseMg(prescription.getDoseMg());

            prescriptionsDTO.getPrescriptions().add(prescriptionDTO);
//        prescriptionDTO.setDaystage(prescription);

        });
        return prescriptionsDTO;
    }
}
