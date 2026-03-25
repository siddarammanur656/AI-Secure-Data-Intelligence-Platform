package com.secureai.AI_Secure_Data_Intelligence_Platform.service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class FileProcessingService {

    // Maximum characters to extract (prevents memory issues for huge files)
    private static final int MAX_CHARS = 500_000;

    private final Tika tika = new Tika();

    public String extractText(MultipartFile file) {
        String filename = file.getOriginalFilename() != null
                ? file.getOriginalFilename().toLowerCase() : "";

        try {
            // .log and .txt files: read directly as UTF-8 text (line by line)
            if (filename.endsWith(".log") || filename.endsWith(".txt")) {
                return readAsPlainText(file);
            }

            // .sql files: read directly as UTF-8
            if (filename.endsWith(".sql")) {
                return readAsPlainText(file);
            }

            // PDF, DOC, DOCX and others: use Apache Tika
            return readWithTika(file);

        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to extract text from file: " + file.getOriginalFilename()
                + " — " + e.getMessage(), e);
        }
    }

    // Plain text reader (UTF-8, line-by-line, chunked)

    private String readAsPlainText(MultipartFile file) throws IOException {
        StringBuilder sb = new StringBuilder();
        int charCount = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
                charCount += line.length();

                // Stop reading if file is too large
                if (charCount > MAX_CHARS) {
                    sb.append("\n[... File truncated after ")
                      .append(MAX_CHARS)
                      .append(" characters for performance ...]");
                    break;
                }
            }
        }
        return sb.toString();
    }

    // Tika reader (PDF, DOC, DOCX etc.)

    private String readWithTika(MultipartFile file) throws Exception {
        try (InputStream stream = file.getInputStream()) {
            String extracted = tika.parseToString(stream);
            // Truncate if too long
            if (extracted != null && extracted.length() > MAX_CHARS) {
                return extracted.substring(0, MAX_CHARS)
                        + "\n[... truncated for performance ...]";
            }
            return extracted;
        }
    }

    // Detect if file is a log file by extension

    public boolean isLogFile(MultipartFile file) {
        String name = file.getOriginalFilename();
        return name != null && (name.endsWith(".log") || name.endsWith(".txt"));
    }

    // Detect if file is a SQL file

    public boolean isSqlFile(MultipartFile file) {
        String name = file.getOriginalFilename();
        return name != null && name.endsWith(".sql");
    }
}
