<div align="center">

# AI Secure Data Intelligence Platform

### *Intelligent AI Gateway · Real-Time Log Analyzer · Risk Engine for Sensitive Data*

[![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.4-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react)](https://react.dev/)
[![MongoDB](https://img.shields.io/badge/MongoDB_Atlas-Database-47A248?style=for-the-badge&logo=mongodb)](https://www.mongodb.com/atlas)
[![Vite](https://img.shields.io/badge/Vite-Build_Tool-646CFF?style=for-the-badge&logo=vite)](https://vitejs.dev/)
[![Netlify](https://img.shields.io/badge/Frontend-Netlify-00C7B7?style=for-the-badge&logo=netlify)](https://securexai.netlify.app)

**Live Demo: [securexai.netlify.app](https://securexai.netlify.app)**

*Built for Hackathon — Original, end-to-end, production-deployed project*

</div>

---

## Problem Statement

In modern software systems, sensitive information such as API keys, passwords, personal data, and credentials leaks silently through log files, database dumps, code repositories, and raw text. Developers and security teams face three core challenges with no unified solution:

- There are no automated tools fast enough to scan log data and text at runtime
- Manual review is slow, inconsistent, and prone to human error
- The consequences — data breaches, credential exposure, and compliance failures — are entirely avoidable

*SQL injection remains an active and widely exploited threat, yet most teams rely on fragmented tools that address only one problem at a time.*

This platform was built to close that gap.

---

## Solution Overview

The *AI Secure Data Intelligence Platform* is a security gateway that processes multiple input types — text, logs, SQL snippets, files, and chat content — in real-time. The pipeline is purpose-built to detect, classify, enforce, and explain security risks without requiring any external AI API.

1. **Detect** — Scans input using a multi-layer detection engine combining regex patterns and a log-specific analyzer
2. **Classify** — Assigns every finding a risk level: `LOW`, `MEDIUM`, `HIGH`, or `CRITICAL`
3. **Enforce** — Applies configurable policies: mask sensitive values or block high-risk content outright
4. **Explain** — Generates human-readable security insights such as *"API key exposed on line 12"*
5. **Store** — Persists all scan results in MongoDB Atlas for audit trails and compliance review

---

## Key Features

<div align="center">

### Multi-Input Processing

| Input Type | Supported Formats |
|---|---|
| Text and Logs | Free text, chat messages, raw log lines |
| File Upload | `.log`, `.txt`, `.pdf`, `.doc`, `.docx`, `.sql` |
| SQL Snippets | Raw query input with injection pattern detection |
| Live Chat | Inline text analysis without file upload |

</div>


### Sensitive Data Detection

The detection engine operates at line-level precision across all input types. It identifies:

- **API Keys** — Named keys (`OPENAI_API_KEY`, `ACCESS_KEY`) and vendor prefixes: OpenAI (`sk-`), AWS (`AKIA`), GitHub (`ghp_`), Slack (`xoxb-`)
- **Passwords and Secrets** — `password=`, `passwd=`, `client_secret=`, `private_key=`
- **PII** — Email addresses, phone numbers (excluding timestamps and date values)
- **Financial Data** — Credit card numbers matching standard formats
- **Tokens** — JWT tokens, Bearer tokens, session tokens
- **Infrastructure Leaks** — Stack traces, debug output, error messages with internal paths
- **Hardcoded Credentials** — Secrets embedded directly in code or configuration files


### Log File Analysis

*Log files are uniquely difficult to scan because they mix structured timestamps, system events, and unstructured error output.* The platform handles this with a dedicated Log Analyzer module:

- Line-by-line parsing with timestamp-aware detection (no false positives on date or time values)
- Detection of repeated failed login attempts and authentication anomalies
- Identification of stack traces and exception leaks
- Log-level anomaly classification (DEBUG leaks, production error exposure)

Log Risk Reference:

| Pattern | Risk Level |
|---|---|
| API key found in log | High |
| Password in log | Critical |
| Email address in log | Low |
| Stack trace or debug output | Medium |


### SQL Injection Detection

The engine scans SQL input for known injection signatures:

- `UNION SELECT` injection attempts
- Boolean injection (`OR 1=1`, `OR '1'='1'`)
- Stacked query attacks using semicolons
- `DROP TABLE`, `TRUNCATE`, and destructive DDL statements
- Time-based blind injection (`SLEEP`, `BENCHMARK`, `WAITFOR DELAY`)
- Sensitive but valid patterns: `SELECT *`, `DELETE FROM`, `INSERT INTO`, `GRANT` statements


### AI Insights Engine

After each scan, the platform generates plain-language security explanations — no JSON parsing required to understand what was found:

- *"Critical: API key exposed in plain text on line 12 — rotate immediately"*
- *"Multiple failed authentication attempts detected — possible brute-force activity"*
- *"SQL injection pattern UNION SELECT found — input rejected"*
- *"Sensitive user data logged in plain text — review logging configuration"*


### Risk Scoring and Policy Enforcement

- Every scan produces a risk score from `0` to `100`, calculated from the severity and count of findings
- Risk levels map to: `LOW (0–30)`, `MEDIUM (31–60)`, `HIGH (61–85)`, `CRITICAL (86–100)`
- **Mask Mode** — Redacts sensitive values in the processed output while allowing the request through
- **Block Mode** — Rejects input entirely when the risk score exceeds the high-risk threshold

---

## Architecture and Processing Flow

<div align="center">

```
User Input (Text / File / SQL / Log / Chat)
         |
         v
+----------------------+
|   Validation Layer   |   Input type verification, size checks, format detection
+----------+-----------+
           |
           v
+----------------------+
|  File Processing     |   Apache Tika extracts readable text from PDF, DOC, TXT
|  Service             |
+----------+-----------+
           |
           v
+------------------------------------------+
|          Detection Engine                |
|   +-----------------+  +-------------+  |
|   | Regex Patterns  |  | Log Analyzer|  |
|   | (credentials,   |  | (timestamp- |  |
|   |  SQL, PII)      |  |  aware)     |  |
|   +-----------------+  +-------------+  |
+----------+-------------------------------+
           |   Findings: type, risk, line, value, context
           v
+----------------------+
|    Risk Engine       |   Calculates composite risk score (0-100)
+----------+-----------+
           |
           v
+----------------------+
|    Policy Engine     |   Applies mask or block based on configuration
+----------+-----------+
           |
           v
+----------------------+
|    Insight Engine    |   Generates human-readable security explanations
+----------+-----------+
           |
           v
+----------------------+
|    MongoDB Atlas     |   Persists full scan result for audit and history
+----------+-----------+
           |
           v
     JSON Response  -->  React Frontend
```

</div>

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React 18, Vite 5, Bootstrap 5, Lucide React |
| Backend | Java 17, Spring Boot 3.2.4 |
| File Parsing | Apache Tika 2.9.1 |
| Database | MongoDB Atlas (Spring Data MongoDB) |
| Detection | Custom Regex Engine with Pattern-based Insight Generation |
| Deployment | Netlify (Frontend), Render (Backend) |
| DevOps | Docker, docker-compose |

---

## Installation and Setup

### Prerequisites

- Java 17 or higher
- Node.js 16 or higher
- Maven 3.9 or higher
- A free MongoDB Atlas account

### Step 1 — Clone the Repository

```bash
git clone https://github.com/siddarammanur656/AI-Secure-Data-Intelligence-Platform.git
cd AI-Secure-Data-Intelligence-Platform
```

### Step 2 — Configure the Backend

Create a `.env` file in the project root:

```env
MONGODB_URI=mongodb+srv://<user>:<password>@cluster0.xxxxx.mongodb.net/secureai_db?retryWrites=true&w=majority
```

### Step 3 — Start the Backend

```bash
mvn spring-boot:run
```

*Backend starts at `http://localhost:8080`*

### Step 4 — Configure the Frontend

```bash
cd frontend
```

Create `frontend/.env`:

```env
VITE_API_BASE_URL=
```

*Leave the value empty to use Vite's built-in proxy to `localhost:8080` during development.*

### Step 5 — Start the Frontend

```bash
npm install
npm run dev
```

*Frontend starts at `http://localhost:5173`*

### Step 6 — Full Stack with Docker

```bash
docker-compose up --build
```

---

## API Reference

### POST `/analyze` — Scan text or SQL

**Request**

```bash
curl -X POST http://localhost:8080/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "input_type": "text",
    "content": "api_key=sk-proj-abc123xyz\npassword=Admin@1234",
    "options": {
      "mask": true,
      "block_high_risk": false,
      "log_analysis": false
    }
  }'
```

**Response**

```json
{
  "risk_score": 85,
  "risk_level": "critical",
  "action": "masked",
  "summary": "2 critical findings: API key and password exposed in plain text.",
  "findings": [
    {
      "type": "api_key",
      "risk": "high",
      "line": 1,
      "value": "sk-proj-abc123xyz",
      "description": "API key found in plain text"
    },
    {
      "type": "password",
      "risk": "critical",
      "line": 2,
      "description": "Password exposed in plain text"
    }
  ],
  "insights": [
    "API key exposed — rotate immediately",
    "Hardcoded password detected — use environment variables"
  ]
}
```

### POST `/upload` — Scan a file

```bash
curl -X POST http://localhost:8080/upload \
  -F "file=@/path/to/logfile.log" \
  -F "mask=true" \
  -F "block_high_risk=false"
```

### GET `/history` — Retrieve scan history

```bash
curl http://localhost:8080/history
```

---

## Live Demo

| Resource | Link |
|---|---|
| Live Application | [securexai.netlify.app](https://securexai.netlify.app) |
| Demo Video | [View Recording](https://drive.google.com/drive/folders/1pxOoy5QCfbVO7VF2ePvelSaZu_DjaxKr?usp=sharing) |

*Test the live deployment using the following input:*

```
OPENAI_API_KEY=sk-proj-abc123xyz789abcdef
password=Admin@1234
SELECT * FROM users WHERE id=1 OR '1'='1'--
```

---

## Repository Structure

```
AI-Secure-Data-Intelligence-Platform/
├── src/main/java/com/secureai/
│   ├── controller/               REST endpoints (Analyze, Upload, History)
│   ├── service/
│   │   ├── DetectionEngine.java       Core regex and pattern detection
│   │   ├── LogAnalyzerService.java    Log-specific analysis module
│   │   ├── RiskEngine.java            Risk score calculation (0-100)
│   │   ├── PolicyEngine.java          Mask and block enforcement
│   │   ├── InsightEngine.java         Human-readable insight generation
│   │   └── FileProcessingService.java Apache Tika file parsing
│   ├── model/                    AnalysisRequest, AnalysisResult, Finding
│   ├── repository/               MongoDB data access layer
│   └── config/                   CORS configuration, MongoDB setup
├── frontend/
│   ├── src/
│   │   ├── components/           ScanInput, ScanResults, Card, Header, Sidebar
│   │   ├── pages/                Dashboard, History
│   │   ├── api.js                Centralized API configuration (reads env vars)
│   │   └── styles/main.css       Glassmorphism UI theme with full responsiveness
│   └── netlify.toml              Netlify build and routing configuration
├── Dockerfile                    Backend Docker image (multi-stage, Java 17)
├── docker-compose.yml            Full-stack service orchestration
├── netlify.toml                  Root Netlify configuration
└── .env.example                  Environment variable reference template
```

---

## Challenges Faced

*Each challenge below was encountered during the hackathon build and resolved before deployment.*

- **Log file false positives** — Timestamps and date values were incorrectly classified as phone numbers. Resolved by rewriting the phone number regex with negative lookahead and lookbehind anchors to exclude date/time contexts.

- **API key pattern coverage** — The initial regex matched only a narrow set of short prefixes. The pattern was extended to cover vendor-specific formats (OpenAI `sk-`, AWS `AKIA`, GitHub `ghp_`, Slack `xoxb-`) and generic named assignments such as `OPENAI_API_KEY=`.

- **Bootstrap and CSS conflicts** — Bootstrap's `.form-control` applies a dark text color that overrides utility classes on dark backgrounds, making textarea text invisible. Resolved by replacing Bootstrap utility classes with explicit inline styles carrying full CSS specificity.

- **Environment variable loading** — Spring Boot's `spring.config.import` with `optional:file:.env[.properties]` syntax correctly reads the `.env` file without throwing errors when the file is absent.

- **CORS for production deployment** — The wildcard origin `*` was replaced with an explicit list of allowed origins to correctly support the production Netlify domain.

---

## Future Improvements

- GPT integration for richer contextual explanations of each finding
- YARA rule support to load and apply custom threat detection signatures dynamically
- Real-time log streaming using WebSockets for live monitoring dashboards
- Role-based access control with multi-user authentication and team collaboration
- CI/CD pipeline with GitHub Actions for automated testing and deployment
- SIEM integration to export findings to Splunk or Elasticsearch

---

## Evaluation Alignment

| Criterion | How This Project Meets It |
|---|---|
| *Problem Solving* | Addresses a real and measurable security problem — sensitive data leakage through logs and code. Provides end-to-end detection, scoring, enforcement, and auditability. |
| *Innovation* | Combines regex-based detection, a dedicated log analysis module, and a human-readable insight engine in a single unified pipeline without any external AI API dependency. |
| *Code Quality* | Follows a clean layered Spring Boot architecture (Controller, Service, Repository). Frontend uses centralized API configuration, environment-based switching, and full Docker support. |
| *Domain Relevance* | Directly applicable to cybersecurity, DevSecOps, and data compliance domains including GDPR and SOC 2. Addresses OWASP Top 10 categories for sensitive data exposure and injection attacks. |

---

## Author

**Siddarama Mallanna Manur**

- GitHub: [github.com/siddarammanur656](https://github.com/siddarammanur656)
- Email: siddarammanur656@gmail.com

---

<div align="center">

If this project was useful, consider leaving a star on the repository.

</div>
