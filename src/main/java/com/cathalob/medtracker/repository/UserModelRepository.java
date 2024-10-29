package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.UserModel;
import org.springframework.data.repository.CrudRepository;

public interface UserModelRepository extends CrudRepository<UserModel, Integer> {
}
