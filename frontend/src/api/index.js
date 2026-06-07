import api, { unwrap } from './axios';

export const authApi = {
  login: (credentials) => api.post('/v1/auth/login', credentials).then(unwrap),
  register: (payload) => api.post('/v1/auth/register', payload).then(unwrap),
  refresh: (refreshToken) => api.post('/v1/auth/refresh', { refreshToken }).then(unwrap),
  logout: () => api.post('/v1/auth/logout').then(unwrap),
};

export const userApi = {
  getProfile: () => api.get('/v1/users/me').then(unwrap),
  updateProfile: (payload) => api.put('/v1/users/me', payload).then(unwrap),
};

export const contactApi = {
  getAll: () => api.get('/v1/contacts').then(unwrap),
  getById: (id) => api.get(`/v1/contacts/${id}`).then(unwrap),
  search: (params) => api.get('/v1/contacts/search', { params }).then(unwrap),
  getInnerCircle: () => api.get('/v1/contacts/inner-circle').then(unwrap),
  create: (payload) => api.post('/v1/contacts', payload).then(unwrap),
  update: (id, payload) => api.put(`/v1/contacts/${id}`, payload).then(unwrap),
  delete: (id) => api.delete(`/v1/contacts/${id}`).then(unwrap),
};

export const categoryApi = {
  getAll: () => api.get('/v1/categories').then(unwrap),
  create: (payload) => api.post('/v1/categories', payload).then(unwrap),
  update: (id, payload) => api.put(`/v1/categories/${id}`, payload).then(unwrap),
  delete: (id) => api.delete(`/v1/categories/${id}`).then(unwrap),
};

export const priorityApi = {
  getAll: () => api.get('/v1/priority-levels').then(unwrap),
  create: (payload) => api.post('/v1/priority-levels', payload).then(unwrap),
  update: (id, payload) => api.put(`/v1/priority-levels/${id}`, payload).then(unwrap),
  delete: (id) => api.delete(`/v1/priority-levels/${id}`).then(unwrap),
};

export const interactionApi = {
  getByContact: (contactId) => api.get(`/v1/interactions/contact/${contactId}`).then(unwrap),
  create: (payload) => api.post('/v1/interactions', payload).then(unwrap),
  recalculateScore: (contactId) =>
    api.post(`/v1/interactions/contact/${contactId}/recalculate-score`).then(unwrap),
  delete: (id) => api.delete(`/v1/interactions/${id}`).then(unwrap),
};

export const reminderApi = {
  getDue: (start, end) =>
    api.get('/v1/reminders/due', { params: { start, end } }).then(unwrap),
  getByContact: (contactId) => api.get(`/v1/reminders/contact/${contactId}`).then(unwrap),
};
