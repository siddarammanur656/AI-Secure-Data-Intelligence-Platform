package com.secureai.AI_Secure_Data_Intelligence_Platform.controller;

import com.secureai.AI_Secure_Data_Intelligence_Platform.model.AnalysisResult;
import com.secureai.AI_Secure_Data_Intelligence_Platform.repository.AnalysisResultRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private final AnalysisResultRepository repository;

    public HistoryController(AnalysisResultRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<AnalysisResult>> getHistory() {
        return ResponseEntity.ok(repository.findByOrderByTimestampDesc());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHistoryById(@PathVariable String id) {
        Optional<AnalysisResult> result = repository.findById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
