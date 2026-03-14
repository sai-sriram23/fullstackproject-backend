package com.example.aiocr.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${poe.api.key}")
    private String poeApiKey;

    @Value("${poe.model.id}")
    private String botName;

    public String generateResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        // Poe provides an OpenAI-compatible API endpoint
        String url = "https://api.poe.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + poeApiKey);

        Map<String, Object> request = new HashMap<>();
        request.put("model", botName);
        
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);
        
        request.put("messages", messages);
        request.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);
            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, String> message = (Map<String, String>) choice.get("message");
                    return message.get("content");
                }
            }
            return "Poe Nexus Error: Deployment cluster returned empty logic set.";
            
        } catch (Exception e) {
            return "Poe Link Failure: " + e.getMessage();
        }
    }
}
