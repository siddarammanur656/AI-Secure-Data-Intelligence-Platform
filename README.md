<div align="center">

# 🛡️ AI Secure Data Intelligence Platform

### *Intelligent AI Gateway · Real-Time Log Analyzer · Risk Engine for Sensitive Data*

[![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.4-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react)](https://react.dev/)
[![MongoDB](https://img.shields.io/badge/MongoDB_Atlas-Database-47A248?style=for-the-badge&logo=mongodb)](https://www.mongodb.com/atlas)
[![Vite](https://img.shields.io/badge/Vite-Build_Tool-646CFF?style=for-the-badge&logo=vite)](https://vitejs.dev/)
[![Deployed on Netlify](https://img.shields.io/badge/Frontend-Netlify-00C7B7?style=for-the-badge&logo=netlify)](https://securexai.netlify.app)

**🌐 Live Demo → [securexai.netlify.app](https://securexai.netlify.app)**

> ⚡ Built for Hackathon — Original, end-to-end, production-deployed project

</div>

---

## 📌 Problem Statement

In modern software systems, sensitive data — API keys, passwords, credentials, PII, and secrets — leaks silently through log files, database dumps, code snippets, and text data. Developers and security teams lack a fast, unified tool to:

- **Scan** logs and documents for sensitive information at runtime
- **Classify** risks automatically without manual review
- **Act** with enforcement policies (masking, blocking) before data reaches production

The result: data breaches, credential leaks, and compliance violations that are entirely preventable.

---

## 💡 Solution Overview

**AI Secure Data Intelligence Platform** is an AI-powered security gateway that scans multiple input types — text, logs, SQL, files, and chat — in real-time. The platform:

1. **Detects** sensitive data using a multi-layer pattern engine (Regex + AI Insights)
2. **Classifies** every finding by risk level: `LOW → MEDIUM → HIGH → CRITICAL`
3. **Enforces** configurable policies: mask sensitive values, or block high-risk content
4. **Explains** findings with AI-generated insights like *"API key exposed in logs"*
5. **Stores** all scan history in MongoDB for audit and compliance tracking

---

## ✨ Key Features

### 📁 Multi-Input Processing
| Input Type | Formats Supported |
|---|---|
| Text / Logs | Free text, chat messages, log lines |
| File Upload | `.log`, `.txt`, `.pdf`, `.doc`, `.docx`, `.sql` |
| SQL Snippets | Raw query input with injection detection |

### 🔍 Sensitive Data Detection Engine
Detects with line-level precision:
- 🔑 **API Keys** — OpenAI (`sk-`), AWS (`AKIA`), GitHub (`ghp_`), Slack (`xoxb-`), and named keys (`OPENAI_API_KEY=...`)
- 🔐 **Passwords & Secrets** — `password=`, `passwd=`, `client_secret=`
- 📧 **PII** — Emails, phone numbers
- 💳 **Credit Card Numbers**
- 🪪 **JWT Tokens** & Bearer tokens
- 🐛 **Stack Traces & Error Leaks**
- 🧨 **Hardcoded Credentials**

### 📊 Log Analysis
Specialized log file processing with:
- Timestamp-aware parsing (no false positives on date/time values)
- Detection of repeated failed login attempts
- Identification of error and exception patterns
- Log-level anomaly detection

### 🛡️ SQL Injection Detection
- `UNION SELECT` injection attempts
- Boolean injection (`OR 1=1`)
- Stacked queries, DROP TABLE, TRUNCATE
- Time-based blind injection (`SLEEP`, `BENCHMARK`)

### 🤖 AI Insights Engine
Generates human-readable security insights per scan:
- *"Critical: API key found exposed in plain text on line 12"*
- *"Multiple failed authentication attempts detected"*
- *"SQL injection pattern UNION SELECT found in input"*

### 📈 Risk Scoring & Policy Enforcement
- Risk scores `0–100` calculated from findings
- Risk levels: `LOW` / `MEDIUM` / `HIGH` / `CRITICAL`
- **Mask Mode**: Redacts sensitive values in processed output
- **Block Mode**: Rejects content that exceeds high-risk threshold

---

## 🏗️ Architecture & Processing Flow

```
User Input (Text / File / SQL / Log)
         │
         ▼
┌─────────────────────┐
│   Validation Layer  │  ← Input type & content checks
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│  File Processing    │  ← Apache Tika extracts text from PDF/DOC/TXT
│  Service            │
└────────┬────────────┘
         │
         ▼
┌────────────────────────────────────────┐
│         Detection Engine               │
│  ┌────────────┐  ┌──────────────────┐  │
│  │ Regex      │  │  Log Analyzer    │  │
│  │ Patterns   │  │  Service         │  │
│  └────────────┘  └──────────────────┘  │
└────────┬───────────────────────────────┘
         │  Findings (type, risk, line, value)
         ▼
┌─────────────────────┐
│   Risk Engine       │  ← Calculates risk score (0–100)
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│   Policy Engine     │  ← Applies mask / block actions
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│   Insight Engine    │  ← Generates AI-style human insights
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│   MongoDB Atlas     │  ← Persists scan result for audit history
└────────┬────────────┘
         │
         ▼
         JSON Response → React Frontend
```

---

## 🧰 Tech Stack

| Layer | Technology |
|---|---|
| **Frontend** | React 18, Vite 5, Bootstrap 5, Lucide React |
| **Backend** | Java 17, Spring Boot 3.2.4 |
| **File Parsing** | Apache Tika 2.9.1 (PDF, DOC, TXT) |
| **Database** | MongoDB Atlas (Spring Data MongoDB) |
| **Detection** | Custom Regex Engine + Pattern-based AI Insights |
| **Deployment** | Netlify (Frontend) + Render (Backend) |
| **DevOps** | Docker, docker-compose |

---

## 🚀 Installation & Setup

### Prerequisites
- Java 17+
- Node.js 16+
- Maven 3.9+
- MongoDB Atlas account (free tier works)

### 1. Clone the Repository
```bash
git clone https://github.com/siddarammanur656/AI-Secure-Data-Intelligence-Platform.git
cd AI-Secure-Data-Intelligence-Platform
```

### 2. Configure Backend Environment
Create a `.env` file in the project root:
```env
MONGODB_URI=mongodb+srv://<user>:<password>@cluster0.xxxxx.mongodb.net/secureai_db?retryWrites=true&w=majority
```

### 3. Run the Backend
```bash
mvn spring-boot:run
```
> Backend starts at `http://localhost:8080`

### 4. Configure Frontend Environment
```bash
cd frontend
```
Create `frontend/.env`:
```env
VITE_API_BASE_URL=
```
> Leave empty to use Vite's dev proxy to `localhost:8080`

### 5. Run the Frontend
```bash
npm install
npm run dev
```
> Frontend starts at `http://localhost:5173`

### 6. Docker (Full Stack)
```bash
# From project root
docker-compose up --build
```

---

## 📡 API Usage

### POST `/analyze` — Scan text or SQL
**Request:**
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

**Response:**
```json
{
  "risk_score": 85,
  "risk_level": "critical",
  "action": "allowed",
  "summary": "2 critical findings detected: API key and password exposed.",
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
    "🚨 API key exposed — rotate immediately",
    "💣 Hardcoded password detected — use environment variables"
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

## 🌐 Live Demo

| | Link |
|---|---|
| 🌍 **Live Application** | [securexai.netlify.app](https://securexai.netlify.app) |
| 📹 **Demo Video** | *[Link to be added]* |

**Try these test inputs on the live site:**
```
OPENAI_API_KEY=sk-proj-abc123xyz789abcdef
password=Admin@1234
SELECT * FROM users WHERE id=1 OR '1'='1'--
```

---

## 📂 Repository Structure

```
AI-Secure-Data-Intelligence-Platform/
├── src/main/java/com/secureai/
│   ├── controller/          # REST endpoints (Analyze, Upload, History)
│   ├── service/
│   │   ├── DetectionEngine.java    # Core regex + pattern detection
│   │   ├── LogAnalyzerService.java # Log-specific analysis
│   │   ├── RiskEngine.java         # Risk scoring (0–100)
│   │   ├── PolicyEngine.java       # Mask / Block enforcement
│   │   ├── InsightEngine.java      # AI-style insight generation
│   │   └── FileProcessingService.java  # Apache Tika file parsing
│   ├── model/               # AnalysisRequest, AnalysisResult, Finding
│   ├── repository/          # MongoDB data access
│   └── config/              # CORS, MongoDB config
├── frontend/
│   ├── src/
│   │   ├── components/      # ScanInput, ScanResults, Card, Header, Sidebar
│   │   ├── pages/           # Dashboard, History
│   │   ├── api.js           # Centralized API config (reads env vars)
│   │   └── styles/main.css  # Custom glassmorphism UI theme
│   └── netlify.toml         # Netlify build config
├── Dockerfile               # Backend Docker image
├── docker-compose.yml       # Full-stack orchestration
├── netlify.toml             # Netlify deployment config
└── .env.example             # Environment variable template
```

---

## ⚡ Challenges Faced

- **False Positives in Log Files**: Timestamps and date strings were incorrectly flagged as phone numbers. Solved using regex lookahead/lookbehind anchors to exclude timestamp contexts.
- **API Key Pattern Coverage**: Initial regex only caught short prefixes. Rewrote to cover vendor-specific formats (OpenAI, AWS, GitHub, Slack) as well as generic named-key patterns.
- **Bootstrap vs Custom CSS Conflicts**: Bootstrap's `.form-control` overrides text colors, causing invisible input text on dark backgrounds. Fixed by moving all visual properties to inline styles with guaranteed specificity.
- **Spring Boot `.env` Loading**: `spring.config.import` with `optional:file:.env[.properties]` format correctly reads the env file without crashing when absent.
- **CORS for Deployment**: Replaced wildcard `*` origin with explicit allowed origins list to support production Netlify URL.

---

## 🔮 Future Improvements

- [ ] **GPT Integration** — Use OpenAI API to generate richer contextual explanations of findings
- [ ] **YARA Rule Support** — Load custom threat detection rules dynamically
- [ ] **Real-Time Streaming** — WebSocket-based live log monitoring
- [ ] **Role-Based Dashboard** — Multi-user authentication and team collaboration
- [ ] **CI/CD Pipeline** — GitHub Actions for automated test + deploy
- [ ] **SIEM Integration** — Export findings to Splunk / Elasticsearch

---

## 🏆 Evaluation Alignment

| Criterion | How This Project Meets It |
|---|---|
| **Problem Solving** | Solves a real, measurable problem: sensitive data leakage through logs and code. Provides automated detection, risk scoring, and policy enforcement. |
| **Innovation** | AI Gateway architecture combining regex detection, log-specific analysis, and insight generation in a unified pipeline — without requiring an external AI API. |
| **Code Quality** | Clean layered Spring Boot architecture (Controller → Service → Repository), centralized API config on frontend, Docker support, environment-based config. |
| **Domain Relevance** | Directly applicable to cybersecurity, DevSecOps, and compliance domains (GDPR, SOC 2). Addresses OWASP Top 10 categories including data exposure and injection. |

---

## 👤 Author

**Siddarammanur**
- 🐙 GitHub: [@siddarammanur656](https://github.com/siddarammanur656)
- 📧 Email: siddarammanur656@gmail.com

---

<div align="center">

**Built with ❤️ for Hackathon | Original Project | Production Deployed**

⭐ Star this repo if you found it useful!

</div>
