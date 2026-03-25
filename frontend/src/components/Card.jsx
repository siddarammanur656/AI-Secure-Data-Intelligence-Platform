import React from 'react';

const Card = ({ children, className = '', title, icon, actionBtn }) => {
  return (
    <div className={`glass-card card bg-dark bg-opacity-50 border border-secondary border-opacity-25 shadow-lg ${className}`} style={{ backdropFilter: 'blur(12px)' }}>
      {(title || icon || actionBtn) && (
        <div className="card-header border-bottom border-secondary border-opacity-25 bg-transparent p-3 d-flex justify-content-between align-items-center">
          <div className="d-flex align-items-center">
            {icon && <span className="card-icon me-2 text-primary">{icon}</span>}
            {title && <h3 className="card-title m-0 fs-5 fw-semibold text-white">{title}</h3>}
          </div>
          {actionBtn && <div>{actionBtn}</div>}
        </div>
      )}
      <div className="card-body p-4">
        {children}
      </div>
    </div>
  );
};

export default Card;
