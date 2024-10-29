package com.cathalob.medtracker.model;

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
    private UserRole role;

}
