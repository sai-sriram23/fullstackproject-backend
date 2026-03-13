package com.example.aiocr.repository;
import com.example.aiocr.model.TranslationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface HistoryRepository extends JpaRepository<TranslationHistory, Long> {
    List<TranslationHistory> findByUsernameOrderByTimestampDesc(String username);
}

