package com.example.aiocr.controller;

import com.example.aiocr.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*") 
public class AIController {

    @Autowired
    private AIService aiService;

    @GetMapping("/config")
    public Map<String, String> getAIConfig() {
        return aiService.getAIConfig();
    }

    @PostMapping("/ollama-proxy")
    public String proxyOllama(@RequestBody Map<String, Object> payload) {
        return aiService.proxyOllama(payload);
    }

    @PostMapping("/chat")
    public Map<String, String> chatWithAI(@RequestBody Map<String, String> payload) {
        String input = payload.get("input");
        String country = payload.get("country");
        String provider = payload.get("provider");
        
        String prompt = "[SYSTEM: Act as a 'Native Cultural Strategist' living in " + country + ". Your goal is to help a foreigner integrate perfectly.] " +
                        "A visitor in your country says: '" + input + "'. " +
                        "Provide a response with 3 sections: " +
                        "1. THE LOCAL LOGIC: Explain the deep cultural 'why' behind this situation. " +
                        "2. THE VIBE CHECK: Describe how a local person would feel or react. " +
                        "3. THE ACTION: Give a step-by-step native strategy to win them over or handle it correctly.";
        
        String response = aiService.generateResponse(prompt, provider);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }

    @PostMapping("/decode")
    public Map<String, Object> decodeSocialInteraction(@RequestBody Map<String, String> payload) {
        String text = payload.get("text");
        String country = payload.get("country");
        String goal = payload.get("goal");
        String provider = payload.get("provider");

        String prompt = "[SYSTEM: Act as a master 'Social Decryption Engine' specialized in " + country + ".] Analyze the hidden layers of this interaction: '" + text + "'. " +
                        "User's goal: '" + goal + "'. " +
                        "You MUST return ONLY a JSON block. " +
                        "JSON Schema: { \"tone\": \"...\", \"politeness\": 0-100, \"intensity\": \"...\", \"subtext\": \"Deep cultural subtext\", \"recommendations\": [\"Casual: ...\", \"Formal: ...\", \"Inquisitive: ...\"] }";

        String response = aiService.generateResponse(prompt, provider);

        // Robust JSON extraction
        String cleaned = response;
        if (response.contains("{") && response.contains("}")) {
            cleaned = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
        }
        cleaned = cleaned.replaceAll("```json", "").replaceAll("```", "").trim();

        Map<String, Object> result = new HashMap<>();
        result.put("rawResponse", cleaned); 
        return result;
    }

    @PostMapping("/assistant")
    public Map<String, String> aiAssistant(@RequestBody Map<String, String> payload) {
        String input = payload.get("input");
        String provider = payload.get("provider");
        
        // General assistant prompt
        String response = aiService.generateResponse(input, provider);
        
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return result;
    }

    @PostMapping("/translate-insight")
    public Map<String, String> getTranslationInsight(@RequestBody Map<String, String> payload) {
        String text = payload.get("text");
        String translation = payload.get("translation");
        String targetLang = payload.get("targetLang");
        String provider = payload.get("provider");

        String prompt = "[SYSTEM: Act as a professional linguist and cultural strategist.] " +
                        "The user translated: '" + text + "' into " + targetLang + " as: '" + translation + "'. " +
                        "1. Is this socially appropriate for an interaction in the target country? " +
                        "2. Are there any 'hidden risks' or 'vibe mismatches' with this specific wording? " +
                        "3. Give 1 native 'Pro Tip' to make it sound 100% realistic. " +
                        "Keep it short and premium.";

        String response = aiService.generateResponse(prompt, provider);
        Map<String, String> result = new HashMap<>();
        result.put("insight", response);
        return result;
    }
}
