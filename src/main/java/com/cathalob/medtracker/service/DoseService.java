package com.cathalob.medtracker.service;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.tracking.Dose;
import com.cathalob.medtracker.repository.DoseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoseService {
    @Autowired
    DoseRepository doseRepository;

    public List<Dose> getDoses(UserModel userModel) {
        return doseRepository.findAll().stream().filter(dose -> dose.getEvaluation().getUserModel().getId().equals(userModel.getId())).toList();
    }

}
