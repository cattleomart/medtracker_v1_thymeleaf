package com.cathalob.medtracker.service;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.enums.DAYSTAGE;
import com.cathalob.medtracker.model.prescription.Medication;
import com.cathalob.medtracker.model.prescription.Prescription;
import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.cathalob.medtracker.model.tracking.Dose;
import com.cathalob.medtracker.repository.DoseRepository;
import com.cathalob.medtracker.repository.MedicationRepository;
import com.cathalob.medtracker.repository.PrescriptionScheduleEntryRepository;
import com.cathalob.medtracker.repository.PrescriptionsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    private final DoseRepository doseRepository;

    public void saveMedications(List<Medication> medicationList) {
        medicationRepository.saveAll(medicationList);

    }

    public List<Medication> getMedications() {
        return medicationRepository.findAll();
    }

    public List<Prescription> getPrescriptions() {
        return prescriptionsRepository.findAll();
    }

    private List<PrescriptionScheduleEntry> getPrescriptionScheduleEntries() {
        return prescriptionScheduleEntryRepository.findAll();
    }

    public List<PrescriptionScheduleEntry> getPatientPrescriptionScheduleEntries(UserModel userModel) {
        return prescriptionScheduleEntryRepository.findAll().stream().filter(pse -> pse.getPrescription().getPatient().getId().equals(userModel.getId()))
                .distinct().toList();
    }

    public Map<Integer, List<PrescriptionScheduleEntry>> getPrescriptionScheduleEntriesByPrescriptionId() {
        return getPrescriptionScheduleEntries()
                .stream().collect(Collectors.groupingBy(prescriptionScheduleEntry -> prescriptionScheduleEntry.getPrescription().getId()));
    }

    public Map<Integer, Prescription> getPrescriptionsById() {
        return getPrescriptions()
                .stream().collect(Collectors.toMap(Prescription::getId, Function.identity()));
    }

    public List<Medication> getPatientMedications(UserModel userModel) {
        return getPrescriptions().stream()
                .filter(m -> m.getPatient().getId().equals(userModel.getId()))
                .map(Prescription::getMedication)
                .distinct()
                .toList();
    }

    public List<DAYSTAGE> getPatientDayStages(UserModel userModel) {
        return this.getPatientPrescriptionScheduleEntries(userModel).stream().map(PrescriptionScheduleEntry::getDayStage).distinct().toList();
    }

    public Map<Integer, List<Medication>> getMedicationById() {
        return medicationRepository.findAll()
                .stream().collect(Collectors.groupingBy(Medication::getId));
    }


    public void savePrescriptions(List<Prescription> newPrescriptions) {
        prescriptionsRepository.saveAll(newPrescriptions);
    }

    public void savePrescriptionScheduleEntries(List<PrescriptionScheduleEntry> newPrescriptionScheduleEntries) {
        prescriptionScheduleEntryRepository.saveAll(newPrescriptionScheduleEntries);
    }

    public void saveDoses(List<Dose> newDoses) {
        doseRepository.saveAll(newDoses);
    }
}
