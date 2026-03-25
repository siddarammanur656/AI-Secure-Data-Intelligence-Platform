package com.secureai.AI_Secure_Data_Intelligence_Platform.service;

import com.secureai.AI_Secure_Data_Intelligence_Platform.model.Finding;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InsightEngine {

    public List<String> generateInsights(List<Finding> findings) {
        List<String> insights = new ArrayList<>();
        if (findings == null || findings.isEmpty()) return insights;

        // Group findings by type for smarter messaging
        Map<String, List<Finding>> byType = findings.stream()
                .collect(Collectors.groupingBy(Finding::getType));

        // SQL Injection
        if (byType.containsKey("sql_injection")) {
            List<Finding> sqlFindings = byType.get("sql_injection");
            String lines = sqlFindings.stream()
                    .map(f -> String.valueOf(f.getLineNumber()))
                    .distinct().collect(Collectors.joining(", "));
            insights.add("🚨 SQL injection pattern detected on line(s) " + lines
                    + " — attacker may be attempting to manipulate your database queries.");

            boolean hasUnion = sqlFindings.stream().anyMatch(f -> f.getDescription().contains("UNION"));
            boolean hasDrop  = sqlFindings.stream().anyMatch(f -> f.getDescription().contains("DROP"));
            boolean hasSleep = sqlFindings.stream().anyMatch(f -> f.getDescription().contains("SLEEP"));

            if (hasUnion) insights.add("⚠️ UNION SELECT detected — attacker may be extracting data from additional tables.");
            if (hasDrop)  insights.add("💣 DROP TABLE/DATABASE command found — this could destroy your entire database.");
            if (hasSleep) insights.add("⏱️ Time-based injection (SLEEP/BENCHMARK) detected — possible blind SQL injection attack.");
        }

        // Sensitive SQL
        if (byType.containsKey("sql_sensitive")) {
            List<Finding> sq = byType.get("sql_sensitive");
            boolean hasSelectAll = sq.stream().anyMatch(f -> f.getDescription().contains("SELECT *"));
            boolean hasDelete    = sq.stream().anyMatch(f -> f.getDescription().contains("DELETE"));
            boolean hasTruncate  = sq.stream().anyMatch(f -> f.getDescription().contains("TRUNCATE"));
            boolean hasGrant     = sq.stream().anyMatch(f -> f.getDescription().contains("GRANT"));

            if (hasSelectAll) insights.add("📋 SELECT * used — exposing all columns increases data leak risk. Use explicit column names.");
            if (hasDelete)    insights.add("🗑️ DELETE FROM detected — ensure a WHERE clause is present to prevent full table deletion.");
            if (hasTruncate)  insights.add("⚠️ TRUNCATE TABLE detected — this is irreversible and will delete all rows.");
            if (hasGrant)     insights.add("🔑 GRANT privileges statement found — unauthorized privilege escalation risk.");
        }

        // Passwords
        if (byType.containsKey("password")) {
            List<Finding> pwds = byType.get("password");
            String lines = pwds.stream().map(f -> String.valueOf(f.getLineNumber()))
                    .distinct().collect(Collectors.joining(", "));
            insights.add("🔐 Hardcoded password(s) found on line(s) " + lines
                    + " — credentials must never appear in plain text. Rotate immediately.");
        }

        // API Keys
        if (byType.containsKey("api_key")) {
            List<Finding> keys = byType.get("api_key");
            String lines = keys.stream().map(f -> String.valueOf(f.getLineNumber()))
                    .distinct().collect(Collectors.joining(", "));
            insights.add("🗝️ API key exposed on line(s) " + lines
                    + " — revoke this key immediately and replace with environment variables.");
        }

        // Secrets 
        if (byType.containsKey("secret")) {
            insights.add("🔒 Hardcoded secret/private key detected — store secrets in a vault (e.g. HashiCorp Vault, AWS Secrets Manager).");
        }

        // Tokens / JWT 
        if (byType.containsKey("token") || byType.containsKey("jwt")) {
            insights.add("🎫 Auth token or JWT found in plain text — tokens in logs or code can be replayed by attackers.");
        }

        // Emails 
        if (byType.containsKey("email")) {
            long count = byType.get("email").size();
            insights.add("📧 " + count + " email address(es) found — personal data exposure may violate GDPR/privacy regulations.");
        }

        // Credit Cards 
        if (byType.containsKey("credit_card")) {
            insights.add("💳 Credit card number detected — this is a severe PCI-DSS violation. Mask or remove immediately.");
        }

        // Suspicious Activity (from LogAnalyzer) 
        if (byType.containsKey("suspicious_activity")) {
            insights.add("🚫 Multiple failed login attempts detected — possible brute-force attack in progress. Consider IP blocking.");
        }

        // Debug Leaks 
        if (byType.containsKey("secret") || findings.stream().anyMatch(f -> "Debug Log Leak".equals(f.getValue()))) {
            insights.add("🐛 Sensitive data leaked via debug logging — set log level to INFO or higher in production.");
        }

        // Stack Traces 
        if (byType.containsKey("stack_trace")) {
            insights.add("📚 Stack trace exposed in logs — reveals internal class structure and file paths to potential attackers.");
        }

        //  IP Addresses
        if (byType.containsKey("ip_address")) {
            long count = byType.get("ip_address").size();
            if (count >= 3) {
                insights.add("🌐 " + count + " IP addresses found in logs — review for suspicious or unauthorized access origins.");
            }
        }

        //  Phone Numbers 
        if (byType.containsKey("phone")) {
            insights.add("📞 Phone number(s) detected — logging PII violates privacy regulations. Mask before logging.");
        }

        //  General high/critical summary if many issues 
        long critical = findings.stream().filter(f -> "critical".equalsIgnoreCase(f.getRisk())).count();
        long high     = findings.stream().filter(f -> "high".equalsIgnoreCase(f.getRisk())).count();
        if (critical >= 2) {
            insights.add(0, "🔴 " + critical + " critical vulnerabilities found — immediate remediation required before deployment.");
        }
        if (high >= 3) {
            insights.add("📊 " + high + " high-risk findings detected — review your secrets management and input validation strategy.");
        }

        return insights;
    }
}
