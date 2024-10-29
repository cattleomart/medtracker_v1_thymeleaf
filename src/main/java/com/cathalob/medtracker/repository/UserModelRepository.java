package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserModelRepository extends JpaRepository<UserModel, Integer> {
}
