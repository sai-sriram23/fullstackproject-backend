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

    @Autowired
    private com.example.aiocr.service.BiometricService biometricService;

    @GetMapping("/biometric/challenge")
    public ResponseEntity<Map<String, String>> getChallenge(@RequestParam String username) {
        String challenge = biometricService.generateChallenge(username);
        Map<String, String> response = new HashMap<>();
        response.put("challenge", challenge);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/biometric/verify")
    public ResponseEntity<Map<String, String>> verifyBiometric(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String responseData = data.get("response");
        boolean success = biometricService.verifyLogin(username, responseData);
        
        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Biometric verification failed");
        return ResponseEntity.status(401).body(response);
    }
}

