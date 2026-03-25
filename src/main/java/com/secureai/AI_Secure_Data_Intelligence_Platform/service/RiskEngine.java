package com.secureai.AI_Secure_Data_Intelligence_Platform.service;

import com.secureai.AI_Secure_Data_Intelligence_Platform.model.AnalysisResult;
import com.secureai.AI_Secure_Data_Intelligence_Platform.model.Finding;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskEngine {

    public void evaluateRisk(AnalysisResult result) {
        List<Finding> findings = result.getFindings();
        if (findings == null || findings.isEmpty()) {
            result.setRiskScore(0);
            result.setRiskLevel("low");
            return;
        }

        int score = 0;
        for (Finding f : findings) {
            score += f.getRiskScore(); // Defined in Finding logic
        }

        // Cap score at 100
        result.setRiskScore(Math.min(score, 100));

        if (score > 80) {
            result.setRiskLevel("critical");
        } else if (score > 50) {
            result.setRiskLevel("high");
        } else if (score > 20) {
            result.setRiskLevel("medium");
        } else {
            result.setRiskLevel("low");
        }
    }
}
