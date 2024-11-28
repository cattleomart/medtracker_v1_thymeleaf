package com.cathalob.medtracker.model.tracking;
import com.cathalob.medtracker.model.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDate;

@Entity(name = "DAILYEVALUATION")
@Data
@IdClass(DailyEvaluation.class)
public class DailyEvaluation {

    @Id
    private LocalDate recordDate;
    @Id
    @Cascade(CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USERMODEL_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel userModel;
}
