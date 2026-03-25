import React, { useState } from 'react';
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import Dashboard from './pages/Dashboard';
import History from './pages/History';
import { Menu, X } from 'lucide-react';

function App() {
  const [activeTab, setActiveTab] = useState('scanner');
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleTabChange = (tab) => {
    setActiveTab(tab);
    setSidebarOpen(false); // close drawer on mobile after navigation
  };

  return (
    <>
      {/* Background canvas */}
      <div className="bg-canvas fixed-top w-100 h-100" style={{ zIndex: -1, backgroundColor: '#090b14' }}>
        <div className="position-absolute w-100 h-100" style={{ backgroundImage: 'radial-gradient(circle at 50% 0%, #1e293b 0%, transparent 60%)', opacity: 0.6 }}></div>
        <div className="noise-overlay position-absolute w-100 h-100 opacity-25" style={{ background: 'url("data:image/svg+xml,%3Csvg viewBox=%220 0 200 200%22 xmlns=%22http://www.w3.org/2000/svg%22%3E%3Cfilter id=%22noiseFilter%22%3E%3CfeTurbulence type=%22fractalNoise%22 baseFrequency=%220.65%22 numOctaves=%223%22 stitchTiles=%22stitch%22/%3E%3C/filter%3E%3Crect width=%22100%25%22 height=%22100%25%22 filter=%22url(%23noiseFilter)%22/%3E%3C/svg%3E")', mixBlendMode: 'overlay' }}></div>
      </div>

      {/* Mobile top bar */}
      <div className="mobile-topbar d-flex align-items-center justify-content-between px-3 py-2">
        <div className="d-flex align-items-center gap-2">
          <svg width="26" height="26" viewBox="0 0 22 22" fill="none">
            <path d="M11 2L3 6V11C3 15.4 6.4 19.5 11 20.9C15.6 19.5 19 15.4 19 11V6L11 2Z" fill="url(#shieldGradM)" />
            <path d="M8 11L10 13L14 9" stroke="white" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" />
            <defs>
              <linearGradient id="shieldGradM" x1="3" y1="2" x2="19" y2="21" gradientUnits="userSpaceOnUse">
                <stop offset="0%" stopColor="#a78bfa" />
                <stop offset="100%" stopColor="#ec4899" />
              </linearGradient>
            </defs>
          </svg>
          <span className="fw-bold text-white" style={{ fontSize: '16px' }}>SecureAI</span>
        </div>
        <button
          className="btn p-1"
          style={{ color: '#c4b5fd', background: 'rgba(167,139,250,0.12)', border: '1px solid rgba(167,139,250,0.25)', borderRadius: '10px' }}
          onClick={() => setSidebarOpen(true)}
          aria-label="Open menu"
        >
          <Menu size={22} />
        </button>
      </div>

      {/* Mobile overlay backdrop */}
      {sidebarOpen && (
        <div
          className="mobile-overlay"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* Main app layout */}
      <div className="app-container">
        {/* Sidebar — desktop: always visible | mobile: slide-in drawer */}
        <div className={`sidebar-wrapper ${sidebarOpen ? 'sidebar-open' : ''}`}>
          <div className="sidebar-close-btn d-flex justify-content-end px-2 pt-2">
            <button
              className="btn p-1"
              style={{ color: '#c4b5fd', background: 'rgba(167,139,250,0.12)', border: '1px solid rgba(167,139,250,0.25)', borderRadius: '10px' }}
              onClick={() => setSidebarOpen(false)}
              aria-label="Close menu"
            >
              <X size={18} />
            </button>
          </div>
          <Sidebar activeTab={activeTab} setActiveTab={handleTabChange} />
        </div>

        {/* Main content */}
        <main className="main-content flex-grow-1">
          <div className="content-inner">
            <Header />
            <div className={activeTab === 'scanner' ? '' : 'd-none'}>
              <Dashboard />
            </div>
            <div className={activeTab === 'history' ? '' : 'd-none'}>
              <History />
            </div>
          </div>
        </main>
      </div>
    </>
  );
}

export default App;
