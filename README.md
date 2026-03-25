# AI Secure Data Intelligence Platform

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen.svg)
![React](https://img.shields.io/badge/React-18-blue.svg)

An AI-powered security scanner for sensitive data detection. This platform provides deep analysis of textual and file-based data to identify, categorize, and report sensitive information, reducing the risk of data leaks and ensuring compliance with security policies.

## 🌟 Key Features

* **Advanced File Parsing:** Extracts text seamlessly from complex file formats, including PDF, DOCX, and TXT, using Apache Tika.
* **Intelligent Detection Engine:** Scans and identifies sensitive data (PII, credentials, secrets) within the extracted text or direct inputs.
* **Risk & Policy Assessment:** Evaluates findings against predefined security policies and calculates comprehensive risk scores.
* **Insight Generation:** Discovers meaningful patterns or anomalies in the analyzed data through an AI-driven insight engine.
* **Audit & History Tracking:** Persists all scan results securely in MongoDB for historical tracking and compliance auditing.
* **Modern Web Interface:** A responsive and intuitive frontend built with React, Vite, Bootstrap, and Lucide React icons.

## 🏗️ Architecture Stack

### Backend
* **Language:** Java 17
* **Framework:** Spring Boot 3.2.4 (Web, test)
* **Database:** MongoDB Atlas (via Spring Data MongoDB)
* **File Parsing:** Apache Tika Core & Parsers (v2.9.1)
* **Utilities:** Lombok, Jackson, Commons IO

### Frontend
* **Library:** React 18.2.0
* **Build Tool:** Vite 5.2.0
* **Styling:** Bootstrap 5.3.3 & React-Bootstrap
* **Icons:** Lucide React

## 🚀 Getting Started

### Prerequisites

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* [Node.js](https://nodejs.org/en/) (v16 or higher)
* [Maven](https://maven.apache.org/) (or use the included wrapper)
* Access to a MongoDB Atlas cluster (ensure your connection string is configured in `application.properties`/`application.yml`).

### Backend Setup

1. Navigate to the project root directory:
   ```bash
   cd AI_Secure_Data_Intelligence_Platform
   ```
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```
3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   *The backend will typically start on `http://localhost:8080`.*

### Frontend Setup

1. Open a new terminal and navigate to the frontend directory:
   ```bash
   cd AI_Secure_Data_Intelligence_Platform/frontend
   ```
2. Install the necessary NPM packages:
   ```bash
   npm install
   ```
3. Start the Vite development server:
   ```bash
   npm run dev
   ```
   *The frontend will typically be accessible at `http://localhost:5173`.*

## 📁 Project Structure

```text
AI_Secure_Data_Intelligence_Platform/
├── src/main/java/com/secureai/AI_Secure_Data_Intelligence_Platform/
│   ├── config/         # Application configurations (CORS, MongoDB)
│   ├── controller/     # REST APIs (Analyze, FileUpload, History)
│   ├── model/          # Data entities (AnalysisRequest, Result, Finding, etc.)
│   ├── repository/     # MongoDB data access layers
│   └── service/        # Core business logic (Detection, Risk, Policy, Insight, File Processing)
├── frontend/           # React frontend application
│   ├── src/            # React components and assets
│   ├── package.json    # Frontend dependencies and scripts
│   └── vite.config.js  # Vite build configuration
└── pom.xml             # Maven dependencies for backend
```

## 🛡️ License

This project is licensed under the MIT License - see the LICENSE file for details.
