package com.secureai.AI_Secure_Data_Intelligence_Platform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnalysisRequest {

    @JsonProperty("input_type")
    private String inputType;

    private String content;

    private AnalysisOptions options;

    private String fileName;

    // No-args constructor
    public AnalysisRequest() {
    }

    // Parameterized constructor without fileName
    public AnalysisRequest(String inputType, String content, AnalysisOptions options) {
        this.inputType = inputType;
        this.content = content;
        this.options = options;
    }

    // Parameterized constructor with fileName
    public AnalysisRequest(String inputType, String content, AnalysisOptions options, String fileName) {
        this.inputType = inputType;
        this.content = content;
        this.options = options;
        this.fileName = fileName;
    }

    // Getters and Setters
    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AnalysisOptions getOptions() {
        return options;
    }

    public void setOptions(AnalysisOptions options) {
        this.options = options;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isValid() {
        if (inputType == null || inputType.trim().isEmpty()) return false;
        if (content == null || content.trim().isEmpty()) return false;
        boolean validType = inputType.equals("text")
                         || inputType.equals("file")
                         || inputType.equals("sql")
                         || inputType.equals("chat")
                         || inputType.equals("log");
        return validType;
    }

    public AnalysisOptions getOptionsOrDefault() {
        return options == null ? new AnalysisOptions(true, true, false) : options;
    }
}
