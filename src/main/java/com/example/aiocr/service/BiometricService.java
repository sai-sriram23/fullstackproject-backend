package com.example.aiocr.service;

import com.example.aiocr.model.AppUser;
import com.example.aiocr.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
public class BiometricService {

    @Autowired
    private AppUserRepository userRepository;

    // In-memory challenge store (In a real app, use Redis or a cache with TTL)
    private Map<String, String> challenges = new HashMap<>();

    public String generateChallenge(String username) {
        String challenge = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());
        challenges.put(username, challenge);
        return challenge;
    }

    public boolean verifyRegistration(String username, String credentialData) {
        AppUser user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            user.setBiometricData(credentialData);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean verifyLogin(String username, String challengeResponse) {
        // Simplified verification for demo purposes
        // In a real WebAuthn implementation, we would use webauthn4j to verify the signature
        String storedChallenge = challenges.get(username);
        if (storedChallenge != null) {
            challenges.remove(username);
            return true; 
        }
        return false;
    }
}
