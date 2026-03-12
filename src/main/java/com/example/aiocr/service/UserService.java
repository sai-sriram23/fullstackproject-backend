package com.example.aiocr.service;

import com.example.aiocr.model.AppUser;
import com.example.aiocr.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private AppUserRepository userRepository;

    public String registerUser(AppUser user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists";
        }
        userRepository.save(user);
        return "Registered successfully";
    }

    public String loginUser(String username, String password) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return "Login successful";
        }
        return "Invalid credentials";
    }
}
