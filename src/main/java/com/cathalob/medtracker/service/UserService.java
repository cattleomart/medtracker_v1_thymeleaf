package com.cathalob.medtracker.service;

import com.cathalob.medtracker.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private static List<UserModel> users = new ArrayList<>();
    private final PasswordEncoder passwordEncoder;

    public void register (UserModel user){
        log.info(user.getPassword());

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        log.info(user.getPassword());
        users.add(user);
    }
    public UserModel findByLogin(String login){
        log.info("findbylogin");
        return users.stream().filter(user -> user.getUsername().equals(login)).findFirst().orElse(null);
    }


}
