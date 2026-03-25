import React from 'react';
import Card from './Card';
import { ShieldAlert, Cpu, AlertTriangle, CheckCircle, Search, Eye } from 'lucide-react';

const ScanResults = ({ result }) => {
  if (!result) return null;

  const { score, level, action, summary, insights, findings, processedContent, contentType } = result;

  // Determine risk colors
  const riskColor = level === 'HIGH' || level === 'CRITICAL' ? 'danger' 
                  : level === 'MEDIUM' ? 'warning' 
                  : 'success';

  return (
    <div className="results-area mt-4 animation-fade-in">
      
      {/* Risk Banner */}
      <div className={`risk-banner glass-card card text-white bg-${riskColor} bg-opacity-25 border border-${riskColor} border-opacity-50 mb-4 p-4 shadow-lg`} style={{ backdropFilter: 'blur(10px)' }}>
        <div className="d-flex justify-content-between align-items-center flex-wrap gap-3">
          <div className="d-flex align-items-center">
            <div className={`risk-icon bg-${riskColor} bg-opacity-25 p-3 rounded-circle me-3`}>
              {level === 'LOW' ? <CheckCircle size={32} className={`text-${riskColor}`} /> : <ShieldAlert size={32} className={`text-${riskColor}`} />}
            </div>
            <div>
              <h3 className="m-0 fs-4 fw-bold">Analysis Complete</h3>
              <p className="m-0 text-white text-opacity-75">{summary || "Scan finished successfully."}</p>
            </div>
          </div>
          
          <div className="d-flex align-items-center gap-4">
            <div className="text-center">
              <div className={`fs-1 fw-bold text-${riskColor} lh-1`}>{score || 0}</div>
              <div className="fs-7 text-uppercase text-white text-opacity-75 fw-medium tracking-wide">Risk Score</div>
            </div>
            <div className="d-flex flex-column gap-2">
              <span className={`badge bg-${riskColor} fs-6 px-3 py-2 rounded-pill`}>
                {level || 'UNKNOWN'} RISK
              </span>
              <span className={`badge ${action === 'BLOCKED' ? 'bg-danger' : 'bg-success'} bg-opacity-75 fs-6 px-3 py-2 rounded-pill`}>
                {action || 'ALLOWED'}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div className="row g-4">
        {/* Insights Panel */}
        <div className="col-12 col-md-4">
          <Card title="AI Insights" icon={<Cpu size={20} />} className="h-100">
            <ul className="list-group list-group-flush bg-transparent">
              {insights && insights.length > 0 ? insights.map((insight, idx) => (
                <li key={idx} className="list-group-item bg-transparent text-white border-secondary border-opacity-25 px-0 py-3 d-flex align-items-start">
                  <span className="me-2 mt-1 text-primary"><Search size={14} /></span>
                  <span className="fs-6 text-white text-opacity-75">{insight}</span>
                </li>
              )) : (
                <li className="list-group-item bg-transparent text-white text-opacity-50 border-0 px-0">No insights available.</li>
              )}
            </ul>
          </Card>
        </div>

        {/* Findings Table */}
        <div className="col-12 col-md-8">
          <Card 
            title="Security Findings" 
            icon={<AlertTriangle size={20}/>} 
            actionBtn={<span className="badge bg-danger rounded-pill px-3 py-2 bg-opacity-25 text-danger border border-danger border-opacity-50">{findings?.length || 0} found</span>}
            className="h-100"
          >
            <div className="table-responsive rounded-3 border border-secondary border-opacity-25">
              <table className="table table-dark table-hover mb-0 bg-transparent">
                <thead className="bg-black bg-opacity-50 text-white text-opacity-75 fs-7 text-uppercase">
                  <tr>
                    <th className="py-3 px-4 border-0 rounded-top-left">Line</th>
                    <th className="py-3 px-4 border-0">Type</th>
                    <th className="py-3 px-4 border-0">Risk</th>
                    <th className="py-3 px-4 border-0 rounded-top-right">Description</th>
                  </tr>
                </thead>
                <tbody className="bg-secondary bg-opacity-10">
                  {findings && findings.length > 0 ? findings.map((f, idx) => (
                    <tr key={idx} className="border-bottom border-secondary border-opacity-25">
                      <td className="py-3 px-4 text-white text-opacity-75 font-monospace">{f.line}</td>
                      <td className="py-3 px-4 text-white fw-medium">{f.type}</td>
                      <td className="py-3 px-4">
                        <span className={`badge bg-${f.risk === 'HIGH' ? 'danger' : f.risk === 'MEDIUM' ? 'warning text-dark' : 'info'}`}>
                          {f.risk}
                        </span>
                      </td>
                      <td className="py-3 px-4 text-white text-opacity-75">{f.desc}</td>
                    </tr>
                  )) : (
                    <tr>
                      <td colSpan="4" className="text-center py-4 text-white text-opacity-50">No security findings detectd.</td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </Card>
        </div>

        {/* Content Viewer */}
        <div className="col-12">
          <Card 
            title="Processed Content Viewer" 
            icon={<Eye size={20} />}
            actionBtn={<span className="badge bg-secondary rounded-pill px-3 py-1 font-monospace">{contentType || 'text'}</span>}
          >
            <div className="bg-black bg-opacity-50 rounded-3 p-4 border border-secondary border-opacity-25 position-relative overflow-hidden">
              <pre className="m-0 text-white text-opacity-75 font-monospace fs-6" style={{ whiteSpace: 'pre-wrap', maxHeight: '400px', overflowY: 'auto' }}>
                {processedContent || "No content to display."}
              </pre>
            </div>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default ScanResults;
