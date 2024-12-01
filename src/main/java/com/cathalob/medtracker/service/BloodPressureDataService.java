package com.cathalob.medtracker.service;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.tracking.BloodPressureReading;
import com.cathalob.medtracker.repository.BloodPressureReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class BloodPressureDataService {
    @Autowired
    BloodPressureReadingRepository bloodPressureReadingRepository;


    public void saveBloodPressureReadings(List<BloodPressureReading> bloodPressureReadings) {
        bloodPressureReadingRepository.saveAll(bloodPressureReadings);
    }

    public List<BloodPressureReading> getBloodPressureReadings(UserModel userModel) {
        return bloodPressureReadingRepository.findAll().stream()
                .filter(bloodPressureReading -> bloodPressureReading.getDailyEvaluation().getUserModel().getId().equals(userModel.getId()))
                .toList();
    }
}
