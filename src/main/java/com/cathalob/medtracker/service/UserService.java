package com.cathalob.medtracker.service;

import com.cathalob.medtracker.model.UserModel;
import com.cathalob.medtracker.model.UserRole;
import com.cathalob.medtracker.repository.UserModelRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class UserService {


    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserModelRepository userModelRepository;

    @PostConstruct
    public void postConstruct() {
        UserModel user = new UserModel();
        user.setRole(UserRole.ADMIN);
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("abc"));
        userModelRepository.save(user);

    }

    public void register(UserModel user) {
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userModelRepository.save(user);
    }

    public UserModel findByLogin(String login) {

        List<UserModel> dbUsers =
                StreamSupport.stream(userModelRepository.findAll().spliterator(), false)
                        .toList();
        return dbUsers.stream().filter(user -> user.getUsername().equals(login))
                .findFirst()
                .orElse(null);
    }
}
