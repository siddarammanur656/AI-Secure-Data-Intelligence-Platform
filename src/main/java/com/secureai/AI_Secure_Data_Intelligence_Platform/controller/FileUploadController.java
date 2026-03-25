package com.secureai.AI_Secure_Data_Intelligence_Platform.controller;

import com.secureai.AI_Secure_Data_Intelligence_Platform.model.*;
import com.secureai.AI_Secure_Data_Intelligence_Platform.service.AnalysisService;
import com.secureai.AI_Secure_Data_Intelligence_Platform.service.FileProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

    private final FileProcessingService fileProcessingService;
    private final AnalysisService analysisService;

    public FileUploadController(FileProcessingService fileProcessingService,
                                AnalysisService analysisService) {
        this.fileProcessingService = fileProcessingService;
        this.analysisService       = analysisService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "mask",           defaultValue = "true")  boolean mask,
            @RequestParam(value = "block_high_risk", defaultValue = "true") boolean blockHighRisk) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded.");
        }

        try {
            // Extract text from the file
            String extractedText = fileProcessingService.extractText(file);

            // Determine input type based on file extension
            String inputType;
            boolean logAnalysis;

            if (fileProcessingService.isLogFile(file)) {
                inputType   = "log";
                logAnalysis = true;   // always run log analyzer for .log/.txt
            } else if (fileProcessingService.isSqlFile(file)) {
                inputType   = "sql";
                logAnalysis = false;
            } else {
                inputType   = "file";
                logAnalysis = false;
            }

            // Build request
            AnalysisOptions options = new AnalysisOptions(mask, blockHighRisk, logAnalysis);
            AnalysisRequest request = new AnalysisRequest(inputType, extractedText, options, file.getOriginalFilename());

            AnalysisResult result = analysisService.analyze(request);
            result.setFileName(file.getOriginalFilename());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing file: " + e.getMessage());
        }
    }
}

