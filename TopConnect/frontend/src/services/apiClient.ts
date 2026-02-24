import axios from 'axios';

const API_BASE_URL = '/api/v1';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
});

// Add request interceptor to include auth token
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle 401 responses
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  signup: (email: string, password: string, role: string, displayName?: string,
          instagramUrl?: string, youtubeUrl?: string, certificateInfo?: string) =>
    apiClient.post('/auth/signup', { email, password, role, displayName, instagramUrl, youtubeUrl, certificateInfo }),
  login: (email: string, password: string) =>
    apiClient.post('/auth/login', { email, password }),
  verifyEmail: (token: string) =>
    apiClient.get(`/auth/verify-email?token=${token}`),
  me: () => apiClient.get('/auth/me'),
};

export const artistAPI = {
  list: (page = 0, size = 10, category?: string) =>
    apiClient.get(`/artists?page=${page}&size=${size}${category ? `&category=${category}` : ''}`),
  get: (id: string) => apiClient.get(`/artists/${id}`),
  search: (query: string, page = 0, size = 10) =>
    apiClient.get(`/artists/search?query=${query}&page=${page}&size=${size}`),
  getFeatured: (page = 0, size = 10) =>
    apiClient.get(`/artists/featured/list?page=${page}&size=${size}`),
  getOfferings: (artistId: string, page = 0, size = 10) =>
    apiClient.get(`/artists/${artistId}/offerings?page=${page}&size=${size}`),
  getCategories: () =>
    apiClient.get('/artists/categories'),
  getMyProfile: () =>
    apiClient.get('/artists/profile/me'),
  updateProfile: (data: any) =>
    apiClient.put('/artists/profile', data),
};

export const offeringAPI = {
  list: (page = 0, size = 10) =>
    apiClient.get(`/offerings?page=${page}&size=${size}`),
  get: (id: string) => apiClient.get(`/offerings/${id}`),
  getMine: () => apiClient.get('/offerings/mine'),
  create: (data: any) => apiClient.post('/offerings', data),
  update: (id: string, data: any) =>
    apiClient.put(`/offerings/${id}`, data),
  delete: (id: string) => apiClient.delete(`/offerings/${id}`),
};

export const bookingAPI = {
  list: (page = 0, size = 10) =>
    apiClient.get(`/bookings?page=${page}&size=${size}`),
  get: (id: string) => apiClient.get(`/bookings/${id}`),
  create: (data: any) => apiClient.post('/bookings', data),
  confirm: (id: string) => apiClient.post(`/bookings/${id}/confirm`),
  cancel: (id: string, reason?: string) =>
    apiClient.post(`/bookings/${id}/cancel?reason=${reason || ''}`),
  complete: (id: string) => apiClient.post(`/bookings/${id}/complete`),
};

export const paymentAPI = {
  createCheckout: (bookingId: string) =>
    apiClient.post(`/payments/create-checkout?bookingId=${bookingId}`),
  get: (id: string) => apiClient.get(`/payments/${id}`),
};

export const chatAPI = {
  list: (page = 0, size = 10) =>
    apiClient.get(`/chats?page=${page}&size=${size}`),
  createOrGetThread: (artistProfileId: string) =>
    apiClient.post('/chats/thread', { artistProfileId }),
  getMessages: (threadId: string, page = 0, size = 20) =>
    apiClient.get(`/chats/${threadId}/messages?page=${page}&size=${size}`),
  sendMessage: (threadId: string, message: string) =>
    apiClient.post(`/chats/${threadId}/messages`, { message }),
  markAsRead: (threadId: string) =>
    apiClient.post(`/chats/${threadId}/mark-read`),
};

export const reviewAPI = {
  submit: (data: { bookingId: string; rating: number; comment?: string; tags?: string[] }) =>
    apiClient.post('/reviews', data),
  getArtistReviews: (artistId: string, page = 0, size = 10) =>
    apiClient.get(`/reviews/artist/${artistId}?page=${page}&size=${size}`),
  getArtistSummary: (artistId: string) =>
    apiClient.get(`/reviews/artist/${artistId}/summary`),
  checkReviewed: (bookingId: string) =>
    apiClient.get(`/reviews/check/${bookingId}`),
};

export const portfolioAPI = {
  getPublic: (userId: string) =>
    apiClient.get(`/portfolio/user/${userId}`),
  getMine: () =>
    apiClient.get('/portfolio/mine'),
  create: (data: any) =>
    apiClient.post('/portfolio', data),
  update: (itemId: string, data: any) =>
    apiClient.put(`/portfolio/${itemId}`, data),
  delete: (itemId: string) =>
    apiClient.delete(`/portfolio/${itemId}`),
};

export const notificationAPI = {
  list: (page = 0, size = 20) =>
    apiClient.get(`/notifications?page=${page}&size=${size}`),
  unreadCount: () =>
    apiClient.get('/notifications/unread-count'),
  markAsRead: (id: string) =>
    apiClient.post(`/notifications/${id}/read`),
  markAllAsRead: () =>
    apiClient.post('/notifications/read-all'),
};

export const adminAPI = {
  dashboard: () =>
    apiClient.get('/admin/dashboard'),
  listUsers: (page = 0, size = 20, role?: string, status?: string) =>
    apiClient.get(`/admin/users?page=${page}&size=${size}${role ? `&role=${role}` : ''}${status ? `&status=${status}` : ''}`),
  updateUserStatus: (userId: string, status: string) =>
    apiClient.put(`/admin/users/${userId}/status`, { status }),
  listArtists: (page = 0, size = 20) =>
    apiClient.get(`/admin/artists?page=${page}&size=${size}`),
  toggleFeatured: (artistId: string) =>
    apiClient.post(`/admin/artists/${artistId}/toggle-featured`),
  listBookings: (page = 0, size = 20, status?: string) =>
    apiClient.get(`/admin/bookings?page=${page}&size=${size}${status ? `&status=${status}` : ''}`),
  listPayments: (page = 0, size = 20, status?: string) =>
    apiClient.get(`/admin/payments?page=${page}&size=${size}${status ? `&status=${status}` : ''}`),
  listCommissions: () =>
    apiClient.get('/admin/commissions'),
  setDefaultCommission: (rate: number) =>
    apiClient.put('/admin/commissions/default', { rate }),
  setArtistCommission: (artistId: string, rate: number) =>
    apiClient.put(`/admin/commissions/artist/${artistId}`, { rate }),
  // Artist Verification
  listPendingVerifications: (page = 0, size = 20) =>
    apiClient.get(`/admin/verifications/pending?page=${page}&size=${size}`),
  verifyArtist: (artistId: string) =>
    apiClient.post(`/admin/verifications/${artistId}/verify`),
  rejectArtist: (artistId: string, reason: string) =>
    apiClient.post(`/admin/verifications/${artistId}/reject`, { reason }),
};

export default apiClient;
