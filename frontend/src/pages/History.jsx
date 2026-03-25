import React, { useState, useEffect } from 'react';
import Card from '../components/Card';
import { History as HistoryIcon, RefreshCw, Filter } from 'lucide-react';
import { API } from '../api';

const History = () => {
  const [historyDocs, setHistoryDocs] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadHistory = async () => {
    setLoading(true);
    try {
      const res = await fetch(API.HISTORY);
      if (!res.ok) {
        if (res.status === 504 || res.status === 502) {
          throw new Error('Cannot connect to Spring Boot backend. Is it running on port 8080?');
        }
        throw new Error('Fetch failed');
      }
      const data = await res.json();

      const mappedDocs = data.map((doc, idx) => ({
        id: idx,
        time: doc.timestamp ? new Date(doc.timestamp).toLocaleString() : '-',
        type: doc.input_type || 'text',
        score: doc.risk_score || 0,
        level: (doc.risk_level || 'LOW').toUpperCase(),
        action: (doc.action || 'ALLOWED').toUpperCase(),
        summary: doc.summary || '—'
      }));
      setHistoryDocs(mappedDocs);
    } catch (e) {
      console.error(e);
      setHistoryDocs([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHistory();
  }, []);

  return (
    <div className="history-page animation-fade-in mb-5">
      <Card
        title="Scan History Logs"
        icon={<HistoryIcon size={20} />}
        actionBtn={
          <div className="d-flex gap-2">
            <button
              className="btn btn-primary btn-sm rounded-pill px-3 d-flex align-items-center bg-opacity-25"
              onClick={loadHistory}
              disabled={loading}
            >
              <RefreshCw size={14} className={`me-2 ${loading ? 'spin' : ''}`} /> Refresh
            </button>
          </div>
        }
      >
        <div className="table-responsive rounded-3 border border-secondary border-opacity-25">
          <table className="table table-dark table-hover mb-0 bg-transparent align-middle">
            <thead className="bg-black bg-opacity-50 text-white text-opacity-75 fs-7 text-uppercase">
              <tr>
                <th className="py-3 px-4 border-0 rounded-top-left">Timestamp</th>
                <th className="py-3 px-4 border-0">Type</th>
                <th className="py-3 px-4 border-0">Risk Score</th>
                <th className="py-3 px-4 border-0">Risk Level</th>
                <th className="py-3 px-4 border-0">Action</th>
                <th className="py-3 px-4 border-0 rounded-top-right">Summary</th>
              </tr>
            </thead>
            <tbody className="bg-secondary bg-opacity-10">
              {loading ? (
                <tr>
                  <td colSpan="6" className="text-center py-5 text-white text-opacity-50">
                    <div className="spinner-border text-primary me-2 spinner-border-sm" role="status"></div>
                    Loading history...
                  </td>
                </tr>
              ) : historyDocs.length > 0 ? (
                historyDocs.map((doc) => (
                  <tr key={doc.id} className="border-bottom border-secondary border-opacity-25 transition-all hover-bg-dark">
                    <td className="py-3 px-4 text-white text-opacity-75 font-monospace">{doc.time}</td>
                    <td className="py-3 px-4">
                      <span className="badge bg-secondary bg-opacity-25 text-white border border-secondary border-opacity-50 rounded-pill px-2">
                        {doc.type}
                      </span>
                    </td>
                    <td className="py-3 px-4 fw-bold text-center">
                      <span className={`text-${doc.level === 'CRITICAL' || doc.level === 'HIGH' ? 'danger' : doc.level === 'MEDIUM' ? 'warning' : 'success'}`}>
                        {doc.score}
                      </span>
                    </td>
                    <td className="py-3 px-4">
                      <span className={`badge bg-${doc.level === 'CRITICAL' || doc.level === 'HIGH' ? 'danger' : doc.level === 'MEDIUM' ? 'warning text-dark' : 'success'}`}>
                        {doc.level}
                      </span>
                    </td>
                    <td className="py-3 px-4">
                      <span className={`badge ${doc.action.includes('BLOCKED') ? 'bg-danger bg-opacity-25 text-danger border border-danger' : 'bg-success bg-opacity-25 text-success border border-success'} rounded-pill`}>
                        {doc.action}
                      </span>
                    </td>
                    <td className="py-3 px-4 text-white text-opacity-75 fs-7">{doc.summary}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="6" className="text-center py-5 text-white text-opacity-50">No scan history available.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </Card>
      <style>{`
        .spin { animation: spin 1s linear infinite; }
        @keyframes spin { 100% { transform: rotate(360deg); } }
      `}</style>
    </div>
  );
};

export default History;
