import React, { useState } from 'react';
import ScanInput from '../components/ScanInput';
import ScanResults from '../components/ScanResults';
import { API } from '../api';

const Dashboard = () => {
  const [isScanning, setIsScanning] = useState(false);
  const [result, setResult] = useState(null);

  const handleAnalyze = async (payload) => {
    setIsScanning(true);
    setResult(null);

    try {
      let response;
      if (payload.type === 'file') {
        if (!payload.file) {
          alert('Please select a file first.');
          setIsScanning(false);
          return;
        }
        const formData = new FormData();
        formData.append('file', payload.file);
        formData.append('mask', payload.options.maskSensitive);
        formData.append('block_high_risk', payload.options.blockRisk);
        response = await fetch(API.UPLOAD, { method: 'POST', body: formData });
      } else {
        if (!payload.content) {
          alert('Please enter some content to analyze.');
          setIsScanning(false);
          return;
        }
        const bodyPayload = {
          input_type: payload.type,
          content: payload.content,
          options: {
            mask: payload.options.maskSensitive,
            block_high_risk: payload.options.blockRisk,
            log_analysis: payload.options.logAnalysis
          }
        };
        response = await fetch(API.ANALYZE, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(bodyPayload)
        });
      }

      if (!response.ok) {
        const errText = await response.text();
        if (response.status === 504 || response.status === 502) {
          throw new Error('Cannot connect to Spring Boot backend. Is it running on port 8080?');
        }
        throw new Error(errText || `Server error (${response.status})`);
      }

      const data = await response.json();
      
      setResult({
        score: data.risk_score || 0,
        level: (data.risk_level || 'LOW').toUpperCase(),
        action: (data.action || 'ALLOWED').toUpperCase(),
        summary: data.summary,
        insights: data.insights || [],
        findings: (data.findings || []).map(f => ({
          line: f.line > 0 ? f.line : '-',
          type: f.type,
          risk: (f.risk || 'LOW').toUpperCase(),
          desc: f.description
        })),
        processedContent: data.processed_content,
        contentType: data.content_type || data.input_type || 'text'
      });
    } catch (err) {
      if (err.message === 'Failed to fetch' || err.message.includes('NetworkError')) {
        alert('Analysis failed: Cannot connect to server. Please ensure the Spring Boot backend is running.');
      } else {
        alert('Analysis failed: ' + err.message);
      }
    } finally {
      setIsScanning(false);
    }
  };

  return (
    <div className="dashboard-page mb-5">
      <ScanInput onAnalyze={handleAnalyze} isScanning={isScanning} />
      
      {/* Loading State Overlay or Component could be placed here, but handled within button for now */}
      
      {result && <ScanResults result={result} />}
    </div>
  );
};

export default Dashboard;
