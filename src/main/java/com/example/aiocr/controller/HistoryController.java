package com.example.aiocr.controller;
import com.example.aiocr.model.TranslationHistory;
import com.example.aiocr.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "*")
public class HistoryController {
    @Autowired
    private HistoryService historyService;
    @PostMapping("/save")
    public TranslationHistory saveHistory(@RequestBody TranslationHistory history) {
        return historyService.saveHistory(history);
    }
    @GetMapping("/{username}")
    public List<TranslationHistory> getHistory(@PathVariable String username) {
        return historyService.getUserHistory(username);
    }
}

