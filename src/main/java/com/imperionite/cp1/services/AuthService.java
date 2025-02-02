// Implement the user registration and authentication logic
package com.imperionite.cp1.services;


import com.imperionite.cp1.entities.User;
import com.imperionite.cp1.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Mark as service layer
public class AuthService {
    @Autowired
    private UserRepository userRepository; // Inject user repository
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Password encoder

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password
        return userRepository.save(user); // Save user to the database
    }

    // Method to handle user login

    public User login(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return existingUser;
        } else {
            return null;
        }
    }

}
