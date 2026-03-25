package com.secureai.AI_Secure_Data_Intelligence_Platform.controller;

import com.secureai.AI_Secure_Data_Intelligence_Platform.model.AnalysisRequest;
import com.secureai.AI_Secure_Data_Intelligence_Platform.model.AnalysisResult;
import com.secureai.AI_Secure_Data_Intelligence_Platform.service.AnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    private final AnalysisService analysisService;

    public AnalyzeController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping
    public ResponseEntity<?> analyzeContent(@RequestBody AnalysisRequest request) {
        if (!request.isValid()) {
            return ResponseEntity.badRequest().body("Invalid request. Ensure input_type and content are valid.");
        }
        try {
            AnalysisResult result = analysisService.analyze(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error during analysis: " + e.getMessage());
        }
    }
}
