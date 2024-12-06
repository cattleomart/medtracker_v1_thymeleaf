package com.cathalob.medtracker.model;

import com.cathalob.medtracker.model.enums.USERROLE;
import jakarta.persistence.*;
import lombok.*;

@Entity(name="USERMODEL")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String username;
    @NonNull
    private String password;

    @NonNull
    @Enumerated(EnumType.STRING)
    private USERROLE role;
}
