package com.cathalob.medtracker.fileupload;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.prescription.Medication;
import com.cathalob.medtracker.model.prescription.Prescription;
import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.cathalob.medtracker.model.tracking.DailyEvaluation;
import com.cathalob.medtracker.model.tracking.DailyEvaluationId;
import com.cathalob.medtracker.model.tracking.Dose;
import com.cathalob.medtracker.service.EvaluationDataService;

import com.cathalob.medtracker.service.PrescriptionsService;
import com.cathalob.medtracker.service.UserService;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImportCache {
    @Getter
    private Map<Integer, UserModel> userModels;

    @Getter
    private Map<Integer, Medication> medications;

    @Getter
    private Map<Integer, Prescription> prescriptions;

    @Getter
    private Map<Integer, PrescriptionScheduleEntry> prescriptionScheduleEntries;

    @Getter
    private Map<Integer, Dose> doses;

    @Getter
    private Map<DailyEvaluationId, DailyEvaluation> dailyEvaluations;

    @Setter
    @Getter
    private UserModel userModel;


    public ImportCache() {
        this.dailyEvaluations = new HashMap<>();
        this.prescriptionScheduleEntries = new HashMap<>();
        this.doses = new HashMap<>();
    }

    public DailyEvaluation getDailyEvaluation(LocalDate localDate){
        return dailyEvaluations.get(this.getDailyEvaluationKey(localDate, userModel));
    }
    public DailyEvaluationId getDailyEvaluationKey(LocalDate localDate, UserModel userModel) {
        return new DailyEvaluationId(userModel.getId(), localDate);
    }
    public PrescriptionScheduleEntry getPrescriptionScheduleEntry(int key){
        return prescriptionScheduleEntries.get(key);
    }

    public void ensureDailyEvaluations(List<LocalDate> dates, EvaluationDataService evaluationDataService) {
        dates.forEach(localDate -> {
                DailyEvaluationId dailyEvaluationKey = this.getDailyEvaluationKey(localDate, userModel);
                if (!dailyEvaluations.containsKey(dailyEvaluationKey)) {
                    DailyEvaluation dailyEvaluation = new DailyEvaluation();
                    dailyEvaluation.setRecordDate(localDate);
                    dailyEvaluation.setUserModel(userModel);
                    evaluationDataService.addDailyEvaluation(dailyEvaluation);
                    dailyEvaluations.put(dailyEvaluationKey, dailyEvaluation);
            };
        });

    }

    public void loadPrescriptions(PrescriptionsService prescriptionsService) {
        if (prescriptions != null && !prescriptions.keySet().isEmpty()) return;
        prescriptions = prescriptionsService.getPrescriptionsById();
    }

    public void loadPrescriptionScheduleEntries(PrescriptionsService prescriptionsService) {
        if (prescriptionScheduleEntries != null && !prescriptionScheduleEntries.keySet().isEmpty()) return;
        prescriptionScheduleEntries = prescriptionsService.getPrescriptionScheduleEntriesById();
    }

    public void loadDailyEvaluations(EvaluationDataService evaluationDataService) {
        if (dailyEvaluations != null && !dailyEvaluations.keySet().isEmpty()) return;
        dailyEvaluations = evaluationDataService.getDailyEvaluationsById();
    }

    public void loadDoses(PrescriptionsService prescriptionsService) {
        if (doses != null && !doses.keySet().isEmpty()) return;
        doses = prescriptionsService.getDosesById();
    }
    public void loadUserModels(UserService userService) {
        if (userModels != null && !userModels.keySet().isEmpty()) return;
        userModels = userService.getUserModelsById();
    }
    public void loadMedications(PrescriptionsService prescriptionsService) {
        if (medications != null && !medications.keySet().isEmpty()) return;
        medications = prescriptionsService.getMedicationsById();
    }
}
