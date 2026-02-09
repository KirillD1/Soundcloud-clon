import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Добавляем токен ко всем запросам
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Auth API
export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
};

// Tracks API
export const tracksAPI = {
  getAll: (params) => api.get('/tracks', { params }),
  getTrending: (params) => api.get('/tracks/trending', { params }),
  search: (query, params) => api.get('/tracks/search', { params: { q: query, ...params } }),
  getById: (id) => api.get(`/tracks/${id}`),
  upload: (formData) => api.post('/tracks', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  }),
  delete: (id) => api.delete(`/tracks/${id}`),
  stream: (id) => `${API_URL}/tracks/${id}/stream`,
};

// Likes API
export const likesAPI = {
  toggle: (trackId) => api.post(`/tracks/${trackId}/like`),
};

// Comments API
export const commentsAPI = {
  getAll: (trackId) => api.get(`/tracks/${trackId}/comments`),
  add: (trackId, data) => api.post(`/tracks/${trackId}/comments`, data),
  delete: (trackId, commentId) => api.delete(`/tracks/${trackId}/comments/${commentId}`),
};

export default api;
