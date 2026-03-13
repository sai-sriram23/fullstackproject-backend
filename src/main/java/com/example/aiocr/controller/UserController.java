package com.example.aiocr.controller;
import com.example.aiocr.model.AppUser;
import com.example.aiocr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody AppUser user) {
        String result = userService.registerUser(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        if (result.equals("Registered successfully")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody AppUser user) {
        String result = userService.loginUser(user.getUsername(), user.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        if (result.equals("Login successful")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(response);
    }
}

