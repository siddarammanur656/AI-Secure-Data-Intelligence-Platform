package com.secureai.AI_Secure_Data_Intelligence_Platform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Finding {
    private String type;
    private String risk;
    @JsonProperty("line")
    private int lineNumber;
    private String value;
    private String description;
    private Integer column;
    private String context;

    public Finding() {}

    public Finding(String type, String risk, int lineNumber, String value, String description, Integer column, String context) {
        this.type = type;
        this.risk = risk;
        this.lineNumber = lineNumber;
        this.value = value;
        this.description = description;
        this.column = column;
        this.context = context;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRisk() { return risk; }
    public void setRisk(String risk) { this.risk = risk; }

    public int getLineNumber() { return lineNumber; }
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getColumn() { return column; }
    public void setColumn(Integer column) { this.column = column; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }

    public int getRiskScore() {
        if (risk == null) return 0;
        switch (risk.toLowerCase()) {
            case "critical": return 40;
            case "high":     return 25;
            case "medium":   return 10;
            case "low":      return 5;
            default:         return 0;
        }
    }

    public boolean isCritical() { return "critical".equalsIgnoreCase(risk); }
    public boolean isHighOrAbove() { return "high".equalsIgnoreCase(risk) || "critical".equalsIgnoreCase(risk); }

    // Manual builder equivalent functionality
    public static FindingBuilder builder() {
        return new FindingBuilder();
    }
    
    public static class FindingBuilder {
        private String type;
        private String risk;
        private int lineNumber;
        private String value;
        private String description;
        private Integer column;
        private String context;

        public FindingBuilder type(String type) { this.type = type; return this; }
        public FindingBuilder risk(String risk) { this.risk = risk; return this; }
        public FindingBuilder lineNumber(int lineNumber) { this.lineNumber = lineNumber; return this; }
        public FindingBuilder value(String value) { this.value = value; return this; }
        public FindingBuilder description(String description) { this.description = description; return this; }
        public FindingBuilder column(Integer column) { this.column = column; return this; }
        public FindingBuilder context(String context) { this.context = context; return this; }
        public Finding build() {
            return new Finding(type, risk, lineNumber, value, description, column, context);
        }
    }
}