package com.cathalob.medtracker.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("USERMODEL")
@Data
public class UserModel {
    @Id
    private Integer id;
    private String username;
    private String password;
    private UserRole role;

}
