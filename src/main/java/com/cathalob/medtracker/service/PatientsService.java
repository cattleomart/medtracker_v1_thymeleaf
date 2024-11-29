package com.cathalob.medtracker.service;

import com.cathalob.medtracker.dto.PrescriptionDTO;
import com.cathalob.medtracker.dto.PrescriptionsDTO;
import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.prescription.Medication;
import com.cathalob.medtracker.model.tracking.BloodPressureReading;
import com.cathalob.medtracker.model.tracking.Dose;
import com.cathalob.medtracker.repository.BloodPressureReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PatientsService {

    @Autowired
    BloodPressureReadingRepository bloodPressureReadingRepository;

    @Autowired
    PrescriptionsService prescriptionsService;
    @Autowired
    BloodPressureDataService bloodPressureDataService;
    @Autowired
    DoseService doseService;


    public PrescriptionsDTO getPrescriptionsDTO() {
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
        bloodPressureDataService.saveBloodPressureReadings(bloodPressureReadings);
    }

    public List<List<Object>> getDoseGraphData(UserModel userModel) {
        List<List<Object>> listData = new ArrayList<>();
        List<Dose> doses = doseService.getDoses(userModel);
        doses.stream().map(Dose::getPrescriptionScheduleEntry).distinct().toList();
//        prescriptionsService.getPrescriptionScheduleEntries()
        HashMap<LocalDate, List<Dose>> byDate = new HashMap<>();
        doses.stream().forEach(dose -> {
            LocalDate localDate = dose.getDoseTime().toLocalDate();
            byDate.putIfAbsent(
                    localDate, new ArrayList<>());
            byDate.get(localDate).add(dose);
        });

//        List<LocalDate> sortedDates = doses.stream().map(dose -> dose.getDoseTime().toLocalDate()).sorted().toList();
//        sortedDates.forEach(localDate -> {
//            byDate.get(localDate).stream().collect(Collectors.groupingBy(Dos))
//
//
//        });
        return listData;
    }
}
