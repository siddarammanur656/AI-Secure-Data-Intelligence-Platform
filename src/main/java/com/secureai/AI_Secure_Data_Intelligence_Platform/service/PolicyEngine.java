package com.secureai.AI_Secure_Data_Intelligence_Platform.service;

import com.secureai.AI_Secure_Data_Intelligence_Platform.model.AnalysisOptions;
import com.secureai.AI_Secure_Data_Intelligence_Platform.model.AnalysisResult;
import com.secureai.AI_Secure_Data_Intelligence_Platform.model.Finding;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyEngine {

    public void applyPolicy(AnalysisResult result, AnalysisOptions options, String originalContent) {
        if (result.isSafe()) {
            result.setAction("allowed");
            result.setProcessedContent(originalContent);
            return;
        }

        if (options.shouldBlock() && result.hasHighRiskFindings()) {
            result.setAction("blocked");
            result.setProcessedContent("Content blocked due to high/critical risk findings.");
            return;
        }

        if (options.shouldMask()) {
            result.setAction("masked");
            result.setProcessedContent(maskContent(originalContent, result.getFindings()));
        } else {
            result.setAction("allowed"); // No mask, no block
            result.setProcessedContent(originalContent);
        }
    }

    private String maskContent(String originalContent, List<Finding> findings) {
        if (originalContent == null) return null;
        String modifiedContent = originalContent;

        for (Finding f : findings) {
            String val = f.getValue();
            if (val != null && !val.isEmpty()) {
                String maskPattern = "***"; 
                if (val.length() > 6) {
                    maskPattern = val.substring(0, 3) + "***" + val.substring(val.length() - 2);
                }
                
                modifiedContent = modifiedContent.replace(val, maskPattern);
                
                // Update context with masked context
                if (f.getContext() != null) {
                    f.setContext(f.getContext().replace(val, maskPattern));
                }
            }
        }
        return modifiedContent;
    }
}
