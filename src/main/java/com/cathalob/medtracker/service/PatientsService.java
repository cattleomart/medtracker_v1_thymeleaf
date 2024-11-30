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
        doses.stream().map(Dose::getPrescriptionScheduleEntry).distinct().toList();
//        prescriptionsService.getPrescriptionScheduleEntries()
        HashMap<LocalDate, List<Dose>> byDate = new HashMap<>();
        doses.stream().forEach(dose -> {
            LocalDate localDate = dose.getDoseTime().toLocalDate();
            byDate.putIfAbsent(
                    localDate, new ArrayList<>());
            byDate.get(localDate).add(dose);
        });

        List<Medication> medicationList = this.getSortedDistinctMedicationsForDoses(doses);
        List<DAYSTAGE> daystageList = this.getSortedDistinctDayStagesFromDoses(doses);

        List<LocalDate> sortedDates = doses.stream().map(dose -> dose.getDoseTime().toLocalDate()).sorted().toList();


        sortedDates.forEach(localDate -> {
            Map<Medication, List<Dose>> byMedication = byDate.get(localDate).stream().
                    sorted(Comparator.comparing(d -> d.getPrescriptionScheduleEntry().getPrescription().getMedication().getName()))
                    .collect(Collectors.groupingBy(dose -> dose.getPrescriptionScheduleEntry().getPrescription().getMedication()));


            List<Object> dayDoseData = new ArrayList<>();
            dayDoseData.add(localDate);
            for (int medIndex = 0; medIndex < medicationList.size(); medIndex++) {
                for (int dsIndex = 0; dsIndex < daystageList.size(); dsIndex++) {

                    int finalMedIndex = medIndex;
                    int finalDsIndex = dsIndex;

                    Medication medicationKey = medicationList.get(medIndex);

                    if (byMedication.containsKey(medicationKey)){
                        DAYSTAGE dsKey = daystageList.get(dsIndex);
                        Map<DAYSTAGE, List<Dose>> byDayStage = byMedication.get(medicationKey).stream()
                                .collect(Collectors.groupingBy(dose -> dose.getPrescriptionScheduleEntry().getDayStage()));
                        if (byDayStage.containsKey(dsKey)) {
                            byDayStage
                                    .get(dsKey).stream().forEach(dose -> dayDoseData.add(finalMedIndex + finalDsIndex + 1, dose.getPrescriptionScheduleEntry().getPrescription().getDoseMg()));
                        } else {
//                            System.out.println("Date: "  + localDate + " MedKey: "  + medicationKey + "DSKey: "  + dsKey);
                            dayDoseData.add(null);
                        }

                    }
                    else {
//                        System.out.println("Date: "  + localDate + " MedKey: "  + medicationKey);
                        dayDoseData.add(null);
                    }

                }
            }
            System.out.println(dayDoseData);
            listData.add(dayDoseData);

        });



            return listData;


    }

    private List<DAYSTAGE> getSortedDistinctDayStagesFromDoses(List<Dose> doses) {
        return doses.stream().map(dose -> dose.getPrescriptionScheduleEntry().getDayStage()).distinct().sorted(Comparator.comparing(DAYSTAGE::toString)).toList();
    }

    public List<List<String>> getDoseGraphColumnNames(UserModel userModel){
        List<Dose> doses = doseService.getDoses(userModel);
        List<String> names = new ArrayList<>();
        List<DAYSTAGE> daystagesFromDoses = this.getSortedDistinctDayStagesFromDoses(doses);
        this.getSortedDistinctMedicationsForDoses(doses).stream().map(Medication::getName).forEach(m -> daystagesFromDoses.forEach(ds -> names.add( m  + " (" + ds.toString() + ')')));
        System.out.println(names);
        return Arrays.asList(names);
    }
    private List<Medication> getSortedDistinctMedicationsForDoses(List<Dose> doses){
        return doses.stream().map(dose -> dose.getPrescriptionScheduleEntry().getPrescription().getMedication()).distinct().sorted(Comparator.comparing(Medication::getName)).toList();
    }

}
