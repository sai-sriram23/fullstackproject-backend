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

    @Value("${ai.provider:poe}")
    private String aiProvider;

    @Value("${poe.api.key}")
    private String poeApiKey;

    @Value("${poe.model.id}")
    private String poeModel;

    @Value("${ollama.api.url}")
    private String ollamaUrl;

    @Value("${ollama.model.id}")
    private String ollamaModel;

    public String generateResponse(String prompt) {
        return generateResponse(prompt, null);
    }

    public String generateResponse(String prompt, String providerOverride) {
        String provider = (providerOverride != null && !providerOverride.isEmpty()) ? providerOverride : aiProvider;
        if ("ollama".equalsIgnoreCase(provider)) {
            return generateOllamaResponse(prompt);
        } else {
            return generatePoeResponse(prompt);
        }
    }

    private String generatePoeResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.poe.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + poeApiKey);

        Map<String, Object> request = new HashMap<>();
        request.put("model", poeModel);
        
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);
        
        request.put("messages", messages);
        request.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            System.out.println("Poe API Request: " + request);
            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);
            System.out.println("Poe API Response: " + response);
            
            if (response != null) {
                if (response.containsKey("choices")) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    if (!choices.isEmpty()) {
                        Map<String, Object> choice = choices.get(0);
                        Map<String, String> message = (Map<String, String>) choice.get("message");
                        return message.get("content");
                    }
                } else if (response.containsKey("error")) {
                    Map<String, Object> error = (Map<String, Object>) response.get("error");
                    return "Poe API Error: " + error.get("message");
                }
            }
            return "Poe Nexus Error: Deployment cluster returned empty logic set.";
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            String errorResponse = e.getResponseBodyAsString();
            System.err.println("Poe API HTTP Error: " + errorResponse);
            return "Poe API HTTP Failure: " + e.getStatusCode() + " - " + errorResponse;
        } catch (Exception e) {
            System.err.println("Poe Link Failure: " + e.getMessage());
            return "Poe Link Failure: " + e.getMessage();
        }
    }

    private String generateOllamaResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> request = new HashMap<>();
        request.put("model", ollamaModel);
        request.put("stream", false);
        
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);
        
        request.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            System.out.println("Ollama Request: " + request);
            Map<String, Object> response = restTemplate.postForObject(ollamaUrl, entity, Map.class);
            System.out.println("Ollama Response: " + response);
            
            if (response != null && response.containsKey("message")) {
                Map<String, String> message = (Map<String, String>) response.get("message");
                return message.get("content");
            }
            return "Ollama Error: Local inference engine returned null result.";
        } catch (Exception e) {
            System.err.println("Ollama Connection Failure: " + e.getMessage());
            return "Ollama Local Failure: " + e.getMessage() + ". Ensure Ollama is running at " + ollamaUrl;
        }
    }
}
