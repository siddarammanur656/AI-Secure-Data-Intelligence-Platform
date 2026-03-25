/**
 * api.js — Central API configuration
 *
 * In development: VITE_API_BASE_URL is empty, so all fetch calls go to '/'
 * which is proxied by Vite to http://localhost:8080 (see vite.config.js).
 *
 * In production: Set VITE_API_BASE_URL in your .env file (or hosting platform's
 * environment variables) to your backend's deployed URL, e.g.:
 *   VITE_API_BASE_URL=https://your-backend.onrender.com
 */
const BASE_URL = import.meta.env.VITE_API_BASE_URL || '';

export const API = {
  ANALYZE: `${BASE_URL}/analyze`,
  UPLOAD: `${BASE_URL}/upload`,
  HISTORY: `${BASE_URL}/history`,
};
