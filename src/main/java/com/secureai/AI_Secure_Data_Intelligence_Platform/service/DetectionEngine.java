package com.secureai.AI_Secure_Data_Intelligence_Platform.service;

import com.secureai.AI_Secure_Data_Intelligence_Platform.model.Finding;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.*;

@Service
public class DetectionEngine {

    // Sensitive Data Patterns 

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}");

    private static final Pattern PHONE_PATTERN =
        Pattern.compile("(?:\\+?\\d[\\s\\-.]?){9,14}\\d");

    private static final Pattern API_KEY_PATTERN =
        Pattern.compile("(?i)(?:api[_\\-]?key|sk|pk|token)[\\s:=]+['\"]?([A-Za-z0-9\\-._~+/]{16,})['\"]?");

    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("(?i)(?:password|passwd|pwd|pass)\\s*[=:]\\s*['\"]?([^'\"\\s]{4,})['\"]?");

    private static final Pattern TOKEN_PATTERN =
        Pattern.compile("(?i)(?:bearer|jwt|access_token|auth_token|session_token)\\s*[=:]?\\s*([A-Za-z0-9\\-._~+/]{20,}=*)");

    private static final Pattern SECRET_PATTERN =
        Pattern.compile("(?i)(?:secret|private[_\\-]?key|client[_\\-]?secret|aws[_\\-]?secret)\\s*[=:]\\s*['\"]?([^'\"\\s]{8,})['\"]?");

    private static final Pattern CREDIT_CARD_PATTERN =
        Pattern.compile("\\b(?:\\d[ \\-]?){13,16}\\b");

    private static final Pattern JWT_PATTERN =
        Pattern.compile("eyJ[A-Za-z0-9\\-_]+\\.eyJ[A-Za-z0-9\\-_]+\\.[A-Za-z0-9\\-_]+");

    // SQL Injection / Sensitive Query Patterns

    // Dangerous SQL injection attempts
    private static final Pattern SQL_INJECTION_UNION =
        Pattern.compile("(?i)\\bUNION\\s+(?:ALL\\s+)?SELECT\\b");

    private static final Pattern SQL_INJECTION_OR_TRUE =
        Pattern.compile("(?i)(?:'\\s*OR\\s*'?1'?\\s*=\\s*'?1|\"\\s*OR\\s*\"?1\"?\\s*=\\s*\"?1|\\bOR\\s+1\\s*=\\s*1)");

    private static final Pattern SQL_INJECTION_COMMENT =
        Pattern.compile("(?:--|#|/\\*|\\*/|;\\s*--\\s*)");

    private static final Pattern SQL_INJECTION_DROP =
        Pattern.compile("(?i)\\bDROP\\s+(?:TABLE|DATABASE|INDEX|VIEW)\\b");

    private static final Pattern SQL_INJECTION_STACKED =
        Pattern.compile("(?i);\\s*(?:SELECT|INSERT|UPDATE|DELETE|DROP|EXEC|EXECUTE)\\b");

    private static final Pattern SQL_INJECTION_SLEEP =
        Pattern.compile("(?i)\\b(?:SLEEP|BENCHMARK|WAITFOR\\s+DELAY)\\s*\\(");

    // Sensitive but valid SQL queries
    private static final Pattern SQL_SELECT_ALL =
        Pattern.compile("(?i)\\bSELECT\\s+\\*\\s+FROM\\b");

    private static final Pattern SQL_INSERT =
        Pattern.compile("(?i)\\bINSERT\\s+INTO\\b");

    private static final Pattern SQL_UPDATE =
        Pattern.compile("(?i)\\bUPDATE\\s+\\w+\\s+SET\\b");

    private static final Pattern SQL_DELETE =
        Pattern.compile("(?i)\\bDELETE\\s+FROM\\b");

    private static final Pattern SQL_GRANT =
        Pattern.compile("(?i)\\bGRANT\\s+(?:ALL|SELECT|INSERT|UPDATE|DELETE)\\b");

    private static final Pattern SQL_TRUNCATE =
        Pattern.compile("(?i)\\bTRUNCATE\\s+TABLE\\b");

    // Main detect() method

    public List<Finding> detect(String content) {
        List<Finding> findings = new ArrayList<>();
        if (content == null || content.isBlank()) return findings;

        String[] lines = content.split("\\r?\\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineNum = i + 1;

            // Sensitive Data
            addMatches(line, lineNum, EMAIL_PATTERN,       "email",       "low",      "Email address exposed",                       findings);
            addMatches(line, lineNum, PHONE_PATTERN,       "phone",       "low",      "Phone number exposed",                        findings);
            addMatches(line, lineNum, API_KEY_PATTERN,     "api_key",     "high",     "API key found in plain text",                 findings);
            addMatches(line, lineNum, PASSWORD_PATTERN,    "password",    "critical", "Password exposed in plain text",              findings);
            addMatches(line, lineNum, TOKEN_PATTERN,       "token",       "high",     "Auth token exposed",                          findings);
            addMatches(line, lineNum, SECRET_PATTERN,      "secret",      "critical", "Hardcoded secret/private key found",          findings);
            addMatches(line, lineNum, CREDIT_CARD_PATTERN, "credit_card", "critical", "Credit card number exposed",                  findings);
            addMatches(line, lineNum, JWT_PATTERN,         "jwt",         "high",     "JWT token found exposed",                     findings);

            // SQL Injection (HIGH risk)
            addMatches(line, lineNum, SQL_INJECTION_UNION,   "sql_injection", "high", "UNION SELECT injection pattern detected",     findings);
            addMatches(line, lineNum, SQL_INJECTION_OR_TRUE, "sql_injection", "high", "OR 1=1 boolean injection detected",           findings);
            addMatches(line, lineNum, SQL_INJECTION_COMMENT, "sql_injection", "high", "SQL comment injection (-- or #) detected",   findings);
            addMatches(line, lineNum, SQL_INJECTION_DROP,    "sql_injection", "high", "DROP TABLE/DATABASE command detected",        findings);
            addMatches(line, lineNum, SQL_INJECTION_STACKED, "sql_injection", "high", "Stacked query injection (;) detected",       findings);
            addMatches(line, lineNum, SQL_INJECTION_SLEEP,   "sql_injection", "high", "Time-based injection (SLEEP/BENCHMARK) detected", findings);

            // Sensitive SQL Queries (MEDIUM risk)
            addMatches(line, lineNum, SQL_SELECT_ALL, "sql_sensitive", "medium", "SELECT * exposes all columns — avoid in production", findings);
            addMatches(line, lineNum, SQL_INSERT,     "sql_sensitive", "medium", "INSERT INTO detected — verify data being inserted",  findings);
            addMatches(line, lineNum, SQL_UPDATE,     "sql_sensitive", "medium", "UPDATE SET detected — ensure WHERE clause exists",   findings);
            addMatches(line, lineNum, SQL_DELETE,     "sql_sensitive", "medium", "DELETE FROM detected — verify WHERE clause exists",  findings);
            addMatches(line, lineNum, SQL_GRANT,      "sql_sensitive", "high",   "GRANT privileges statement detected",                findings);
            addMatches(line, lineNum, SQL_TRUNCATE,   "sql_sensitive", "high",   "TRUNCATE TABLE detected — irreversible operation",   findings);
        }

        return findings;
    }

    // Helper: add all regex matches as Findings

    private void addMatches(String line, int lineNum, Pattern pattern,
                             String type, String risk, String description,
                             List<Finding> findings) {
        Matcher m = pattern.matcher(line);
        while (m.find()) {
            // Use capture group 1 if available (e.g. password value only), else full match
            String value = m.groupCount() >= 1 && m.group(1) != null
                    ? m.group(1)
                    : m.group();

            findings.add(Finding.builder()
                    .type(type)
                    .risk(risk)
                    .lineNumber(lineNum)
                    .value(value)
                    .description(description)
                    .column(m.start())
                    .context(line.length() > 120 ? line.substring(0, 120) + "..." : line)
                    .build());
        }
    }
}
