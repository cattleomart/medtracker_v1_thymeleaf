package com.cathalob.medtracker.model.tracking;
import com.cathalob.medtracker.model.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import java.time.LocalDate;

@Entity(name = "DAILYEVALUATION")
@Data
@IdClass(DailyEvaluationId.class)
public class DailyEvaluation {

    @Id
    private LocalDate recordDate;
    @Id
    @Cascade(CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USERMODEL_ID", nullable = false)
    @JsonIgnore
    private UserModel userModel;



    public DailyEvaluationId getDailyEvaluationIdClass() {
        return new DailyEvaluationId(userModel.getId(), recordDate);
    }
}
