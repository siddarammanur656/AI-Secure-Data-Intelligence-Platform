import React, { useState } from 'react';
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import Dashboard from './pages/Dashboard';
import History from './pages/History';

function App() {
  const [activeTab, setActiveTab] = useState('scanner');

  return (
    <>
      <div className="bg-canvas fixed-top w-100 h-100" style={{ zIndex: -1, backgroundColor: '#090b14' }}>
        <div className="position-absolute w-100 h-100" style={{ backgroundImage: 'radial-gradient(circle at 50% 0%, #1e293b 0%, transparent 60%)', opacity: 0.6 }}></div>
        <div className="noise-overlay position-absolute w-100 h-100 opacity-25" style={{ background: 'url("data:image/svg+xml,%3Csvg viewBox=%220 0 200 200%22 xmlns=%22http://www.w3.org/2000/svg%22%3E%3Cfilter id=%22noiseFilter%22%3E%3CfeTurbulence type=%22fractalNoise%22 baseFrequency=%220.65%22 numOctaves=%223%22 stitchTiles=%22stitch%22/%3E%3C/filter%3E%3Crect width=%22100%25%22 height=%22100%25%22 filter=%22url(%23noiseFilter)%22/%3E%3C/svg%3E")', mixBlendMode: 'overlay' }}></div>
      </div>

      <div className="app-container d-flex min-vh-100 container-fluid p-0">
        <div className="col-auto" style={{ width: '260px' }}>
          <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />
        </div>
        
        <main className="main-content flex-grow-1 p-4 d-flex flex-column" style={{ overflowY: 'auto', maxHeight: '100vh' }}>
          <div className="container-fluid max-w-7xl mx-auto">
            <Header />
            
            <div className={`tab-content fade ${activeTab === 'scanner' ? 'show active' : 'd-none'}`}>
              <Dashboard />
            </div>
            
            <div className={`tab-content fade ${activeTab === 'history' ? 'show active' : 'd-none'}`}>
              <History />
            </div>
          </div>
        </main>
      </div>
    </>
  );
}

export default App;
