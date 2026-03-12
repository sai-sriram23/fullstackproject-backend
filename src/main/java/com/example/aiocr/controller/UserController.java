package com.example.aiocr.controller;

import com.example.aiocr.model.AppUser;
import com.example.aiocr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody AppUser user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody AppUser user) {
        return userService.loginUser(user.getUsername(), user.getPassword());
    }
}
