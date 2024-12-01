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

        TreeMap<LocalDate, List<Dose>> byDate = doses.stream()
                .sorted(Comparator.comparing(dose -> dose.getDoseTime().toLocalDate()))
                .collect(Collectors.groupingBy(dose -> dose.getDoseTime().toLocalDate(), TreeMap::new, Collectors.toList()));

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
        return doses.stream().map(dose -> dose.getPrescriptionScheduleEntry().getDayStage()).distinct().sorted(Comparator.comparing(DAYSTAGE::ordinal)).toList();
    }

    public List<List<Object>> getSystoleGraphData(UserModel userModel){
        List<List<Object>> listData = new ArrayList<>();
        List<BloodPressureReading> bloodPressureReadings = bloodPressureDataService.getBloodPressureReadings(userModel);

        Map<LocalDate, List<BloodPressureReading>> byDate = bloodPressureReadings.stream().sorted(Comparator.comparing(bloodPressureReading -> bloodPressureReading.getReadingTime().toLocalDate()))
                .collect(Collectors.groupingBy(bloodPressureReading -> bloodPressureReading.getReadingTime().toLocalDate()));
        List<DAYSTAGE> sortedDayStages = bloodPressureReadings.stream().map(BloodPressureReading::getDayStage).distinct().sorted(Comparator.comparing(DAYSTAGE::ordinal)).toList();

        byDate.forEach((date, bloodPressureReadingsByDate) -> {
            ArrayList<Object> dayList = new ArrayList<>();
            dayList.add(date);
            dayList.add(140);
            dayList.add(130);
            dayList.add(120);
            Map<DAYSTAGE, List<BloodPressureReading>> bprMap = bloodPressureReadingsByDate.stream().collect(Collectors.groupingBy(BloodPressureReading::getDayStage));

            for (DAYSTAGE dayStage : sortedDayStages)
            {
                if (bprMap.containsKey(dayStage)) {
                    dayList.add(bprMap.get(dayStage).stream().mapToInt(BloodPressureReading::getSystole).sum());
                }
                else {
                    dayList.add(null);
                }
            }
            listData.add(dayList);
        });

        return listData;
    }

    public List<List<String>> getSystoleGraphColumnNames(UserModel userModel){
        List<String> names = new ArrayList<>();
        names.add("Danger High");
        names.add("High Stage 1");
        names.add("Elevated");
        List<DAYSTAGE> bloodPressureReadings = bloodPressureDataService.getBloodPressureReadings(userModel).
                stream().map(BloodPressureReading::getDayStage)
                .distinct()
                .toList();
        names.addAll(this.prettifiedDayStageNames(bloodPressureReadings.stream().sorted(Comparator.comparing(DAYSTAGE::ordinal)).toList()));
        return List.of(names);
    }

    public List<List<String>> getDoseGraphColumnNames(UserModel userModel) {
        List<Dose> doses = doseService.getDoses(userModel);
        List<String> names = new ArrayList<>();
        List<String> dayStageNames = this.prettifiedDayStageNames(this.getSortedDistinctDayStagesFromDoses(doses));

        for (String medication : this.getSortedDistinctMedicationsForDoses(doses).stream().map(Medication::getName).toList()){
            for (String dayStage : dayStageNames){
                names.add(medication + " (" + dayStage + ')');
            }
        }
        return List.of(names);
    }

    private List<String> prettifiedDayStageNames(List<DAYSTAGE> dayStages) {
        return dayStages.stream().map(ds -> (
                ds.toString().charAt(0) + ds.toString().substring(1).toLowerCase())).toList();
    }

    private List<Medication> getSortedDistinctMedicationsForDoses(List<Dose> doses) {
        return doses.stream().map(dose -> dose.getPrescriptionScheduleEntry().getPrescription().getMedication()).distinct().sorted(Comparator.comparing(Medication::getName)).toList();
    }

}
