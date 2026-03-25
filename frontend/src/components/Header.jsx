import React from 'react';
import { ShieldCheck, Zap } from 'lucide-react';

const Header = () => {
  return (
    <header className="top-header pb-4 mb-4 border-bottom border-secondary border-opacity-25 d-flex justify-content-between align-items-center flex-wrap gap-3">
      <div className="header-text">
        <h1 className="header-title display-6 fw-bold text-white mb-2">
          AI Data Intelligence <span className="title-accent text-primary">Scanner</span>
        </h1>
        <p className="header-sub text-secondary m-0 fs-5">
          Detect sensitive data, credentials, SQL injections & security threats with AI-powered analysis.
        </p>
      </div>
      <div className="header-badges d-flex gap-2">
        <span className="hbadge badge-secure bg-primary bg-opacity-10 border border-primary border-opacity-25 text-primary rounded-pill px-3 py-2 d-flex align-items-center fw-medium font-monospace shadow-sm">
          <ShieldCheck size={16} className="me-2" />
          End-to-End Secured
        </span>
        <span className="hbadge badge-realtime bg-success bg-opacity-10 border border-success border-opacity-25 text-success rounded-pill px-3 py-2 d-flex align-items-center fw-medium font-monospace shadow-sm">
          <Zap size={16} className="me-2" />
          Real-time Analysis
        </span>
      </div>
    </header>
  );
};

export default Header;
