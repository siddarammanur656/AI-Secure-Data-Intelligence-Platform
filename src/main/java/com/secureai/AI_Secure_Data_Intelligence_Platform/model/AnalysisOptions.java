package com.secureai.AI_Secure_Data_Intelligence_Platform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnalysisOptions {
    @JsonProperty("mask")
    private boolean mask = true;

    @JsonProperty("block_high_risk")
    private boolean blockHighRisk = true;

    @JsonProperty("log_analysis")
    private boolean logAnalysis = false;

    public AnalysisOptions() {}

    public AnalysisOptions(boolean mask, boolean blockHighRisk, boolean logAnalysis) {
        this.mask = mask;
        this.blockHighRisk = blockHighRisk;
        this.logAnalysis = logAnalysis;
    }

    public boolean isMask() { return mask; }
    public void setMask(boolean mask) { this.mask = mask; }

    public boolean isBlockHighRisk() { return blockHighRisk; }
    public void setBlockHighRisk(boolean blockHighRisk) { this.blockHighRisk = blockHighRisk; }

    public boolean isLogAnalysis() { return logAnalysis; }
    public void setLogAnalysis(boolean logAnalysis) { this.logAnalysis = logAnalysis; }

    public boolean shouldMask() { return mask; }
    public boolean shouldBlock() { return blockHighRisk; }
    public boolean shouldAnalyzeLogs() { return logAnalysis; }

    public static AnalysisOptions defaultOptions() { return new AnalysisOptions(true, true, false); }
    public static AnalysisOptions forLogAnalysis() { return new AnalysisOptions(true, true, true); }
}
