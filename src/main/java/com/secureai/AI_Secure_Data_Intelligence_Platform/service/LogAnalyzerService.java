package com.secureai.AI_Secure_Data_Intelligence_Platform.service;

import com.secureai.AI_Secure_Data_Intelligence_Platform.model.Finding;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogAnalyzerService {

    private static final Pattern IP_PATTERN = Pattern.compile("\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b");
    private static final Pattern STACK_TRACE_PATTERN = Pattern.compile("(?i)(Exception.*(?<=\\n).*\\b(at\\s+.*\\(.*?\\)))");

    public List<Finding> analyze(String content) {
        List<Finding> logFindings = new ArrayList<>();
        if (content == null || content.isEmpty()) {
            return logFindings;
        }

        String[] lines = content.split("\\r?\\n");
        int failedLoginAttempts = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].toLowerCase();
            int lineNumber = i + 1;

            // Simple brute force detection logic based on "fail" or "error" near "login"
            if (line.contains("login") && (line.contains("fail") || line.contains("error") || line.contains("denied"))) {
                failedLoginAttempts++;
            }

            if (line.contains("debug") && (line.contains("password") || line.contains("key") || line.contains("secret"))) {
                logFindings.add(Finding.builder()
                    .type("secret")
                    .risk("high")
                    .lineNumber(lineNumber)
                    .value("Debug Log Leak")
                    .description("Debug mode log leaking credentials or secrets")
                    .context(lines[i])
                    .build());
            }

            Matcher ipMatcher = IP_PATTERN.matcher(lines[i]);
            while (ipMatcher.find()) {
                logFindings.add(Finding.builder()
                    .type("ip_address")
                    .risk("medium")
                    .lineNumber(lineNumber)
                    .value(ipMatcher.group())
                    .description("IP Address exposed in logs")
                    .context(lines[i])
                    .build());
            }
        }

        if (failedLoginAttempts >= 3) {
            logFindings.add(Finding.builder()
                .type("suspicious_activity")
                .risk("high")
                .lineNumber(0)
                .value("Multiple Login Failures")
                .description("Detected " + failedLoginAttempts + " failed login attempts, possible brute-force attack.")
                .context("Aggregated from multiple lines")
                .build());
        }

        return logFindings;
    }
}
