package com.cathalob.medtracker.service;

import com.cathalob.medtracker.dto.PrescriptionDTO;
import com.cathalob.medtracker.dto.PrescriptionsDTO;
import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.enums.DAYSTAGE;
import com.cathalob.medtracker.model.prescription.Medication;
import com.cathalob.medtracker.model.tracking.BloodPressureReading;
import com.cathalob.medtracker.model.tracking.Dose;
import com.cathalob.medtracker.repository.BloodPressureReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
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

        TreeMap<LocalDate, List<Dose>> byDate = new TreeMap<>();
        doses.stream().sorted(Comparator.comparing(dose -> dose.getDoseTime().toLocalDate())).forEach(dose -> {
            LocalDate localDate = dose.getDoseTime().toLocalDate();
            byDate.putIfAbsent(
                    localDate, new ArrayList<>());
            byDate.get(localDate).add(dose);
        });

        List<Medication> medicationList = this.getSortedDistinctMedicationsForDoses(doses);
        List<DAYSTAGE> daystageList = this.getSortedDistinctDayStagesFromDoses(doses);

        byDate.forEach((localDate, doseByDate) -> {
            List<Object> dayDoseData = new ArrayList<>();
            Map<Medication, List<Dose>> byMedication = doseByDate.stream()
                    .collect(Collectors.groupingBy(dose -> dose.getPrescriptionScheduleEntry().getPrescription().getMedication()));

            dayDoseData.add(localDate);
            for (Medication medication : medicationList) {
                for (DAYSTAGE daystage : daystageList) {
                    if (byMedication.containsKey(medication)) {
                        Map<DAYSTAGE, List<Dose>> byDayStage = byMedication.get(medication).stream()
                                .collect(Collectors.groupingBy(dose -> dose.getPrescriptionScheduleEntry().getDayStage()));
                        if (byDayStage.containsKey(daystage)) {
                            dayDoseData.add(byDayStage
                                    .get(daystage).stream().mapToInt(value -> value.getPrescriptionScheduleEntry().getPrescription().getDoseMg()).sum());
                        } else {
                            dayDoseData.add(null);
                        }
                    } else {
                        dayDoseData.add(null);
                    }
                }
            }
            listData.add(dayDoseData);
        });
        return listData;
    }

    private List<DAYSTAGE> getSortedDistinctDayStagesFromDoses(List<Dose> doses) {
        return doses.stream().map(dose -> dose.getPrescriptionScheduleEntry().getDayStage()).distinct().sorted(Comparator.comparing(DAYSTAGE::toString)).toList();
    }

    public List<List<String>> getDoseGraphColumnNames(UserModel userModel) {
        List<Dose> doses = doseService.getDoses(userModel);
        List<String> names = new ArrayList<>();
        List<DAYSTAGE> daystagesFromDoses = this.getSortedDistinctDayStagesFromDoses(doses);
        this.getSortedDistinctMedicationsForDoses(doses).stream().map(Medication::getName).forEach(m -> daystagesFromDoses.forEach(ds -> names.add(m + " (" + ds.toString() + ')')));
        System.out.println(names);
        return Arrays.asList(names);
    }

    private List<Medication> getSortedDistinctMedicationsForDoses(List<Dose> doses) {
        return doses.stream().map(dose -> dose.getPrescriptionScheduleEntry().getPrescription().getMedication()).distinct().sorted(Comparator.comparing(Medication::getName)).toList();
    }

}
