package com.java.coreTemplate.service;

import com.java.coreTemplate.model.entity.UserDetails;
import com.java.coreTemplate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
        public UserDetails registerUser(String username, String password, String email) {
            if (userRepository.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Username already exists");
            }
            if (userRepository.findByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Email already exists");
            }

            UserDetails newUser = new UserDetails(username, password, email);
            return userRepository.save(newUser);
        }

        public Optional<UserDetails> authenticateUser(String username, String password) {
            Optional<UserDetails> userOptional = userRepository.findByUsername(username);
            return userOptional.filter(user -> user.getPassword().equals(password));
        }
    }