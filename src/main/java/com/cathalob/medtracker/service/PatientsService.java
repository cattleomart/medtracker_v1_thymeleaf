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
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        List<Medication> medicationList = prescriptionsService.getPatientMedications(userModel).stream().sorted(Comparator.comparing(Medication::getName)).toList();
        List<DAYSTAGE> daystageList = prescriptionsService.getPatientDayStages(userModel).stream().sorted(Comparator.comparing(DAYSTAGE::ordinal)).toList();
System.out.println(medicationList);
System.out.println(daystageList);


        LocalDate end = LocalDate.now().plusDays(1);
        Optional<LocalDate> start = byDate.keySet().stream().distinct().min(LocalDate::compareTo);
        List<LocalDate> daysRange = new ArrayList<>();
        if (start.isPresent()) {
            long numDays = ChronoUnit.DAYS.between(start.get(), end);
            daysRange.addAll(Stream.iterate(start.get(), date -> date.plusDays(1)).limit(numDays).toList());
        }

        for (LocalDate date : daysRange){
            List<Dose> dosesByDate = (byDate.get(date) == null) ? new ArrayList<>() : byDate.get(date);

            List<Object> dayDoseData = new ArrayList<>();
            Map<Medication, List<Dose>> byMedication = dosesByDate.stream()
                    .collect(Collectors.groupingBy(dose -> dose.getPrescriptionScheduleEntry().getPrescription().getMedication()));

            dayDoseData.add(date);
            for (Medication medication : medicationList) {
                for (DAYSTAGE daystage : daystageList) {
                    if (byMedication.containsKey(medication)) {
                        Map<DAYSTAGE, List<Dose>> byDayStage = byMedication.get(medication).stream()
                                .collect(Collectors.groupingBy(dose -> dose.getPrescriptionScheduleEntry().getDayStage()));
                        List<Dose> doseEntriesForDayStage = byDayStage
                                .get(daystage);
                        if (byDayStage.containsKey(daystage)) {
                            if (doseEntriesForDayStage.stream().filter(Dose::isTaken).toList().isEmpty()) {
                                dayDoseData.add(0);
                            }
                            else {
                                dayDoseData.add(doseEntriesForDayStage.stream().filter(Dose::isTaken).mapToInt(value -> value.getPrescriptionScheduleEntry().getPrescription().getDoseMg()).sum());
                            }
                        } else {
                            dayDoseData.add(null);
                        }
                    } else {
                        dayDoseData.add(null);
                    }
                }
            }
            listData.add(dayDoseData);
        }
        return listData;
    }

    public List<List<Object>> getSystoleGraphData(UserModel userModel){
        List<List<Object>> listData = new ArrayList<>();
        List<BloodPressureReading> bloodPressureReadings = bloodPressureDataService.getBloodPressureReadings(userModel);

        TreeMap<LocalDate, List<BloodPressureReading>> byDate = bloodPressureReadings.stream().sorted(Comparator.comparing(bloodPressureReading -> bloodPressureReading.getReadingTime().toLocalDate()))
                .collect(Collectors.groupingBy(bloodPressureReading -> bloodPressureReading.getReadingTime().toLocalDate(), TreeMap::new, Collectors.toList()));

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
                    OptionalDouble average = bprMap.get(dayStage).stream().mapToInt(BloodPressureReading::getSystole).average();
                    dayList.add(average.isEmpty() ? null : ((int) average.getAsDouble()));
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
        List<String> names = new ArrayList<>();
        List<String> dayStageNames = this.prettifiedDayStageNames(prescriptionsService.getPatientDayStages(userModel));

        for (String medication : prescriptionsService.getPatientMedications(userModel).stream().map(Medication::getName).toList()){
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
}
