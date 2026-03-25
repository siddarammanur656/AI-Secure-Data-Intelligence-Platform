import React from 'react';
import { Shield, Target, History, ScanSearch } from 'lucide-react';

const Sidebar = ({ activeTab, setActiveTab }) => {
  return (
    <aside className="sidebar glass-panel d-flex flex-column p-3">
      <div className="logo d-flex align-items-center mb-4">
        <div className="logo-icon me-2">
          {/* Reusing the gradient shield SVG but wrapped nicely */}
          <svg width="32" height="32" viewBox="0 0 22 22" fill="none">
            <path d="M11 2L3 6V11C3 15.4 6.4 19.5 11 20.9C15.6 19.5 19 15.4 19 11V6L11 2Z" fill="url(#shieldGrad)" />
            <path d="M8 11L10 13L14 9" stroke="white" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" />
            <defs>
              <linearGradient id="shieldGrad" x1="3" y1="2" x2="19" y2="21" gradientUnits="userSpaceOnUse">
                <stop offset="0%" stopColor="#a78bfa" />
                <stop offset="100%" stopColor="#ec4899" />
              </linearGradient>
            </defs>
          </svg>
        </div>
        <div className="logo-text">
          <h2 className="logo-title m-0 fs-5 fw-bold text-white">SecureAI</h2>
          <span className="logo-sub text-muted font-monospace" style={{ fontSize: '0.75rem' }}>Intelligence Platform</span>
        </div>
      </div>

      <nav className="nav-menu flex-grow-1">
        <a 
          href="#" 
          className={`nav-item d-flex align-items-center p-2 mb-2 rounded text-decoration-none ${activeTab === 'scanner' ? 'active bg-white bg-opacity-10 text-white' : 'text-secondary'}`}
          onClick={(e) => { e.preventDefault(); setActiveTab('scanner'); }}
        >
          <span className="nav-icon me-3"><ScanSearch size={20} /></span>
          <span className="nav-label fw-medium">Data Scanner</span>
        </a>
        <a 
          href="#" 
          className={`nav-item d-flex align-items-center p-2 mb-2 rounded text-decoration-none ${activeTab === 'history' ? 'active bg-white bg-opacity-10 text-white' : 'text-secondary'}`}
          onClick={(e) => { e.preventDefault(); setActiveTab('history'); }}
        >
          <span className="nav-icon me-3"><History size={20} /></span>
          <span className="nav-label fw-medium">Scan History</span>
        </a>
      </nav>

      <div className="sidebar-footer mt-auto p-3 rounded bg-dark bg-opacity-50 border border-secondary border-opacity-25 d-flex align-items-center">
        <span className="status-dot bg-success rounded-circle me-2" style={{ width: '8px', height: '8px' }}></span>
        <span className="status-text text-light fs-6 fw-medium">System Active</span>
      </div>
    </aside>
  );
};

export default Sidebar;
