package com.imperionite.cp1.initializers;

import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class UserInitializer implements ApplicationRunner {
     
    @Autowired
    private PasswordEncoder encoder;

    private final UserRepository userRepository;

    public UserInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        // Check if the user table is empty
        if (userRepository.count() == 0) {
            // Load initial data
            List<User> users = Arrays.asList(
                new User(null, "superuser", encoder.encode("passworD#1"), true, true, true, null, null),
                new User(null, "admin", encoder.encode("passworD#1"), true, true, false, null, null),
                new User(null, "user0", encoder.encode("passworD#1"), true, false, false, null, null),
                new User(null, "user1", encoder.encode("passworD#1"), true, false, false, null, null),
                new User(null, "user2", encoder.encode("passworD#1"), true, false, false, null, null),
                new User(null, "user3", encoder.encode("passworD#1"), true, false, false, null, null),
                new User(null, "user4", encoder.encode("passworD#1"), true, false, false, null, null),
                new User(null, "user5", encoder.encode("passworD#1"), true, false, false, null, null)
                
            );

            userRepository.saveAll(users);
            System.out.println("Database initialized with default users.");
        } else {
            System.out.println("Database already contains users.");
        }
    }
}