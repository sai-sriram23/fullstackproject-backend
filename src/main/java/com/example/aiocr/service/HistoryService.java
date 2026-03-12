package com.example.aiocr.service;

import com.example.aiocr.model.TranslationHistory;
import com.example.aiocr.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    public TranslationHistory saveHistory(TranslationHistory history) {
        return historyRepository.save(history);
    }

    public List<TranslationHistory> getUserHistory(String username) {
        return historyRepository.findByUsernameOrderByTimestampDesc(username);
    }
}
