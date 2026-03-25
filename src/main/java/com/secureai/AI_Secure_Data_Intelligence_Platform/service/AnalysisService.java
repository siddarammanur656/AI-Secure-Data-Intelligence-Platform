package com.secureai.AI_Secure_Data_Intelligence_Platform.service;

import com.secureai.AI_Secure_Data_Intelligence_Platform.model.*;
import com.secureai.AI_Secure_Data_Intelligence_Platform.repository.AnalysisResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisService {

    private final DetectionEngine detectionEngine;
    private final LogAnalyzerService logAnalyzerService;
    private final RiskEngine riskEngine;
    private final PolicyEngine policyEngine;
    private final InsightEngine insightEngine;
    private final AnalysisResultRepository repository;

    public AnalysisService(DetectionEngine detectionEngine,
                           LogAnalyzerService logAnalyzerService,
                           RiskEngine riskEngine,
                           PolicyEngine policyEngine,
                           InsightEngine insightEngine,
                           AnalysisResultRepository repository) {
        this.detectionEngine    = detectionEngine;
        this.logAnalyzerService = logAnalyzerService;
        this.riskEngine         = riskEngine;
        this.policyEngine       = policyEngine;
        this.insightEngine      = insightEngine;
        this.repository         = repository;
    }

    public AnalysisResult analyze(AnalysisRequest request) {
        // These getters now work perfectly following the fix in AnalysisRequest
        String content        = request.getContent();
        String inputType      = request.getInputType();
        AnalysisOptions opts  = request.getOptionsOrDefault();

        List<Finding> allFindings = new ArrayList<>();

        // 1. Always run the core detection engine (handles all types including SQL)
        allFindings.addAll(detectionEngine.detect(content));

        // 2. For "log" input type OR when log_analysis option is ON → run log analyzer
        boolean isLogType = "log".equalsIgnoreCase(inputType);
        if (isLogType || opts.shouldAnalyzeLogs()) {
            allFindings.addAll(logAnalyzerService.analyze(content));
        }

        // 3. Determine content type label for the response
        String contentType = switch (inputType.toLowerCase()) {
            case "sql"  -> "sql";
            case "log"  -> "logs";
            case "file" -> "document";
            case "chat" -> "chat";
            default     -> "text";
        };

        // 4. Build initial result
        AnalysisResult result = AnalysisResult.builder()
                .inputType(inputType)
                .contentType(contentType)
                .findings(allFindings)
                .build();

        // 5. Risk Engine — calculate score and level
        riskEngine.evaluateRisk(result);

        // 6. Policy Engine — mask or block
        policyEngine.applyPolicy(result, opts, content);

        // 7. Insight Engine — smart messages
        result.setInsights(insightEngine.generateInsights(allFindings));

        // 8. Generate summary
        result.setSummary(buildSummary(result, inputType));

        // 9. Save to MongoDB and return
        return repository.save(result);
    }

    private String buildSummary(AnalysisResult result, String inputType) {
        if (result.isSafe()) {
            return "No sensitive data detected. Content appears safe.";
        }
        if ("blocked".equals(result.getAction())) {
            return "Content BLOCKED: " + result.getTotalFindings()
                    + " security issue(s) detected including critical/high risk findings.";
        }
        long critical = result.getCriticalCount();
        long high     = result.getHighCount();
        int  total    = result.getTotalFindings();
        String typeLabel = "sql".equalsIgnoreCase(inputType) ? "SQL" : inputType;

        return String.format("Found %d issue(s) in %s input: %d critical, %d high risk. Action: %s.",
                total, typeLabel, critical, high, result.getAction());
    }
}
