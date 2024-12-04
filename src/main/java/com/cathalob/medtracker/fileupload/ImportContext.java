package com.cathalob.medtracker.fileupload;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.prescription.Prescription;
import com.cathalob.medtracker.model.prescription.PrescriptionScheduleEntry;
import com.cathalob.medtracker.model.tracking.DailyEvaluation;
import com.cathalob.medtracker.model.tracking.DailyEvaluationId;
import com.cathalob.medtracker.model.tracking.Dose;
import com.cathalob.medtracker.service.EvaluationDataService;
import com.cathalob.medtracker.service.PrescriptionsService;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImportContext {

    private Map<Integer, Prescription> prescriptions;
    @Getter
    private Map<Integer, PrescriptionScheduleEntry> prescriptionScheduleEntries;
    private EvaluationDataService evaluationDataService;
    @Getter
    private Map<DailyEvaluationId, DailyEvaluation> dailyEvaluations;
    @Getter
    private Map<Integer, Dose> doses;

    private PrescriptionsService prescriptionsService;


    public ImportContext(EvaluationDataService evaluationDataService, PrescriptionsService prescriptionsService) {
        this.dailyEvaluations = new HashMap<>();
        this.prescriptionScheduleEntries = new HashMap<>();
        this.doses = new HashMap<>();
        this.evaluationDataService = evaluationDataService;
        this.prescriptionsService = prescriptionsService;

        this.prescriptions = prescriptionsService.getPrescriptionsById();
        this.prescriptionScheduleEntries = prescriptionsService.getPrescriptionScheduleEntriesById();
        this.dailyEvaluations = evaluationDataService.getDailyEvaluationsById();
    }

    public DailyEvaluationId getDailyEvaluationKey(LocalDate localDate, UserModel userModel) {
        return new DailyEvaluationId(userModel.getId(), localDate);
    }
    public PrescriptionScheduleEntry getPrescriptionScheduleEntry(int key){
        return prescriptionScheduleEntries.get(key);
    }

    public void ensureDailyEvaluations(List<LocalDate> dates, List<UserModel> userModels) {
        dates.forEach(localDate -> {
            userModels.forEach(userModel -> {
                DailyEvaluationId dailyEvaluationKey = this.getDailyEvaluationKey(localDate, userModel);
                if (!dailyEvaluations.containsKey(dailyEvaluationKey)) {
                    DailyEvaluation dailyEvaluation = new DailyEvaluation();
                    dailyEvaluation.setRecordDate(localDate);
                    dailyEvaluation.setUserModel(userModel);
                    evaluationDataService.addDailyEvaluation(dailyEvaluation);
                    dailyEvaluations.put(dailyEvaluationKey, dailyEvaluation);
                }
            });
        });

    }

    public void saveDoses(List<Dose> newDoses) {
        prescriptionsService.saveDoses(newDoses);
    }
}
