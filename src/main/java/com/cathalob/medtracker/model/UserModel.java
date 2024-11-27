package com.cathalob.medtracker.model;

import com.cathalob.medtracker.model.enums.USERROLE;
import jakarta.persistence.*;
import lombok.Data;

@Entity(name="USERMODEL")
@Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private USERROLE role;

}
