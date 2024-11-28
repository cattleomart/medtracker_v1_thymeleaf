package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.tracking.Dose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoseRepository extends JpaRepository<Dose, Integer> {
}
