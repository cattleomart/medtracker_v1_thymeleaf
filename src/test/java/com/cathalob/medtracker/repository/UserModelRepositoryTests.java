package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.UserModel;
import static com.cathalob.medtracker.testdata.UserModelBuilder.aUserModel;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class UserModelRepositoryTests {

    @Autowired
    private UserModelRepository userModelRepository;
    @Test
    public void givenUserModel_whenSaved_thenReturnSavedUserModel(){
        UserModel userModel = aUserModel().build();

        UserModel saved = userModelRepository.save(aUserModel().build());

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(1);
        assertThat(saved.getPassword()).isEqualTo("abc");


    }
}