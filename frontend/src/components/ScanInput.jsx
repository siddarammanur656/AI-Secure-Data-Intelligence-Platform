import React, { useState, useRef } from 'react';
import { AlignLeft, Database, FileText, UploadCloud, ShieldAlert, Cpu } from 'lucide-react';
import Card from './Card';

const ScanInput = ({ onAnalyze, isScanning }) => {
  const [activeTab, setActiveTab] = useState('text');
  const [textContent, setTextContent] = useState('');
  const [sqlContent, setSqlContent] = useState('');
  const [file, setFile] = useState(null);
  
  const [options, setOptions] = useState({
    maskSensitive: true,
    blockRisk: true,
    logAnalysis: false
  });

  const fileInputRef = useRef(null);

  const handleFileChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      setFile(e.dataTransfer.files[0]);
    }
  };

  const handleAnalyzeClick = () => {
    let payload = { type: activeTab, options };
    if (activeTab === 'text') payload.content = textContent;
    else if (activeTab === 'sql') payload.content = sqlContent;
    else if (activeTab === 'file') payload.file = file;
    
    onAnalyze(payload);
  };

  return (
    <Card className="input-card mb-4" style={{ zIndex: 10 }}>
      {/* Tabs */}
      <div className="nav nav-pills mb-4 gap-2 border-bottom border-secondary border-opacity-25 pb-3">
        <button 
          className={`nav-link d-flex align-items-center rounded-pill px-4 py-2 fw-medium ${activeTab === 'text' ? 'bg-primary text-white shadow' : 'bg-dark bg-opacity-50 text-secondary'}`}
          onClick={() => setActiveTab('text')}
        >
          <AlignLeft size={16} className="me-2" /> Text / Logs
        </button>
        <button 
          className={`nav-link d-flex align-items-center rounded-pill px-4 py-2 fw-medium ${activeTab === 'sql' ? 'bg-primary text-white shadow' : 'bg-dark bg-opacity-50 text-secondary'}`}
          onClick={() => setActiveTab('sql')}
        >
          <Database size={16} className="me-2" /> SQL Snippet
        </button>
        <button 
          className={`nav-link d-flex align-items-center rounded-pill px-4 py-2 fw-medium ${activeTab === 'file' ? 'bg-primary text-white shadow' : 'bg-dark bg-opacity-50 text-secondary'}`}
          onClick={() => setActiveTab('file')}
        >
          <FileText size={16} className="me-2" /> File Upload
        </button>
      </div>

      {/* Input Areas */}
      <div className="input-section active position-relative mb-4">
        {activeTab === 'text' && (
          <div className="textarea-wrapper">
            <textarea 
              className="form-control bg-dark bg-opacity-50 text-white border-secondary border-opacity-50 rounded-3 p-3 shadow-inner resize-none font-monospace"
              style={{ minHeight: '180px' }}
              placeholder="Paste your text, logs, or chat messages here for analysis...&#10;&#10;Example:&#10;api_key = sk-abc123xyz789&#10;password=MySecret123"
              value={textContent}
              onChange={(e) => setTextContent(e.target.value)}
            />
            <div className="d-flex justify-content-between mt-2 text-secondary fs-7">
              <span>{textContent.length} characters</span>
              <button className="btn btn-link text-secondary p-0 text-decoration-none" onClick={() => setTextContent('')}>✕ Clear</button>
            </div>
          </div>
        )}

        {activeTab === 'sql' && (
          <div className="textarea-wrapper">
            <div className="sql-label text-warning fw-medium mb-2 d-flex align-items-center">
              <Database size={14} className="me-2" /> SQL Query / Snippet
            </div>
            <textarea 
              className="form-control bg-dark text-warning border-warning border-opacity-50 rounded-3 p-3 font-monospace shadow-inner mb-2"
              style={{ minHeight: '180px' }}
              placeholder="SELECT * FROM users WHERE id=1 OR '1'='1'&#10;UNION SELECT username, password FROM admin--"
              value={sqlContent}
              onChange={(e) => setSqlContent(e.target.value)}
            />
            <div className="text-muted fs-7">💡 Detects SQL injection, dangerous queries, and data exposure</div>
          </div>
        )}

        {activeTab === 'file' && (
          <div 
            className="drop-zone border border-dashed border-2 border-primary border-opacity-50 rounded-4 p-5 text-center bg-primary bg-opacity-10 transition-all cursor-pointer hover-bg-primary-opacity-20"
            onDragOver={(e) => e.preventDefault()}
            onDrop={handleDrop}
            onClick={() => fileInputRef.current?.click()}
          >
            <UploadCloud size={48} className="text-primary mb-3 mx-auto" strokeWidth={1.5} />
            <h4 className="text-white mb-2 fs-5">Drop your file here</h4>
            <p className="text-secondary fs-6 mb-4">Supports .log, .txt, .pdf, .doc, .sql — Max 50MB</p>
            <input type="file" className="d-none" ref={fileInputRef} onChange={handleFileChange} />
            <button className="btn btn-outline-primary rounded-pill px-4">Browse Files</button>
            {file && <div className="mt-3 text-success fw-medium d-flex align-items-center justify-content-center"><FileText size={16} className="me-2" /> {file.name}</div>}
          </div>
        )}
      </div>

      {/* Options Bar */}
      <div className="options-bar bg-black bg-opacity-50 rounded-4 p-3 d-flex flex-wrap gap-4 mb-4 align-items-center">
        <div className="form-check form-switch d-flex align-items-center">
          <input className="form-check-input mt-0 me-2" type="checkbox" role="switch" id="optMask" checked={options.maskSensitive} onChange={(e) => setOptions({...options, maskSensitive: e.target.checked})} />
          <label className="form-check-label text-white fw-medium" htmlFor="optMask">Mask Sensitive Data</label>
        </div>
        <div className="form-check form-switch d-flex align-items-center">
          <input className="form-check-input mt-0 me-2" type="checkbox" role="switch" id="optBlock" checked={options.blockRisk} onChange={(e) => setOptions({...options, blockRisk: e.target.checked})} />
          <label className="form-check-label text-white fw-medium" htmlFor="optBlock">Block High Risk</label>
        </div>
        <div className="form-check form-switch d-flex align-items-center">
          <input className="form-check-input mt-0 me-2" type="checkbox" role="switch" id="optLog" checked={options.logAnalysis} onChange={(e) => setOptions({...options, logAnalysis: e.target.checked})} />
          <label className="form-check-label text-white fw-medium" htmlFor="optLog">Log AI Analysis</label>
        </div>
      </div>

      {/* Analyze Button */}
      <button 
        className="btn btn-primary w-100 py-3 rounded-4 fw-bold fs-5 shadow-lg d-flex align-items-center justify-content-center hover-scale transition-all"
        onClick={handleAnalyzeClick}
        disabled={isScanning}
        style={{
          background: 'linear-gradient(135deg, #a78bfa 0%, #3b82f6 100%)',
          border: 'none',
          boxShadow: '0 10px 25px -5px rgba(59, 130, 246, 0.5)'
        }}
      >
        {isScanning ? (
          <>
            <span className="spinner-border spinner-border-sm me-3" role="status" aria-hidden="true"></span>
            Analyzing with SecureAI...
          </>
        ) : (
          <>
            <Cpu size={22} className="me-2" />
            Run AI Analysis
          </>
        )}
      </button>
    </Card>
  );
};

export default ScanInput;
