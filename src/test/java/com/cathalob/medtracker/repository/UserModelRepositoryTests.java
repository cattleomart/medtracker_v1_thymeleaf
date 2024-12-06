package com.cathalob.medtracker.repository;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.enums.USERROLE;
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
        UserModel userModel = new UserModel();
        userModel.setUsername("name");
        userModel.setPassword("abc");
        userModel.setRole(USERROLE.USER);

        UserModel saved = userModelRepository.save(userModel);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getPassword()).isEqualTo("abc");


    }
}