package com.secureai.AI_Secure_Data_Intelligence_Platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "analysis_results")
public class AnalysisResult {
    @Id
    private String id;

    @JsonProperty("input_type")
    private String inputType;

    private String summary;

    @JsonProperty("content_type")
    private String contentType;

    private List<Finding> findings = new ArrayList<>();

    @JsonProperty("risk_score")
    private int riskScore;

    @JsonProperty("risk_level")
    private String riskLevel;

    private String action;

    private List<String> insights = new ArrayList<>();

    @JsonProperty("processed_content")
    private String processedContent;

    @JsonProperty("file_name")
    private String fileName;

    private LocalDateTime timestamp = LocalDateTime.now();

    public AnalysisResult() {}
    
    public AnalysisResult(String id, String inputType, String summary, String contentType, List<Finding> findings, int riskScore, String riskLevel, String action, List<String> insights, String processedContent, String fileName, LocalDateTime timestamp) {
        this.id = id;
        this.inputType = inputType;
        this.summary = summary;
        this.contentType = contentType;
        this.findings = findings != null ? findings : new ArrayList<>();
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.action = action;
        this.insights = insights != null ? insights : new ArrayList<>();
        this.processedContent = processedContent;
        this.fileName = fileName;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getInputType() { return inputType; }
    public void setInputType(String inputType) { this.inputType = inputType; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public List<Finding> getFindings() { return findings; }
    public void setFindings(List<Finding> findings) { this.findings = findings; }

    public int getRiskScore() { return riskScore; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public List<String> getInsights() { return insights; }
    public void setInsights(List<String> insights) { this.insights = insights; }

    public String getProcessedContent() { return processedContent; }
    public void setProcessedContent(String processedContent) { this.processedContent = processedContent; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getTotalFindings() { return findings == null ? 0 : findings.size(); }

    public long getCriticalCount() {
        if (findings == null) return 0;
        return findings.stream().filter(f -> "critical".equalsIgnoreCase(f.getRisk())).count();
    }

    public long getHighCount() {
        if (findings == null) return 0;
        return findings.stream().filter(f -> "high".equalsIgnoreCase(f.getRisk())).count();
    }

    public boolean hasHighRiskFindings() {
        if (findings == null) return false;
        return findings.stream().anyMatch(Finding::isHighOrAbove);
    }

    public boolean isSafe() { return findings == null || findings.isEmpty(); }

    public static AnalysisResultBuilder builder() {
        return new AnalysisResultBuilder();
    }

    public static class AnalysisResultBuilder {
        private String inputType;
        private String contentType;
        private List<Finding> findings;

        public AnalysisResultBuilder inputType(String inputType) { this.inputType = inputType; return this; }
        public AnalysisResultBuilder contentType(String contentType) { this.contentType = contentType; return this; }
        public AnalysisResultBuilder findings(List<Finding> findings) { this.findings = findings; return this; }
        
        public AnalysisResult build() {
            AnalysisResult result = new AnalysisResult();
            result.setInputType(inputType);
            result.setContentType(contentType);
            if (findings != null) {
                result.setFindings(findings);
            }
            return result;
        }
    }
}