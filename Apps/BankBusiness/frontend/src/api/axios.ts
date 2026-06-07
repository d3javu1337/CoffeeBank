import axios from 'axios';

// const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:1337';

const api = axios.create({
    // baseURL: API_URL,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    },
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                const response = await axios.get(`/api/auth/refresh`, {
                    withCredentials: true,
                });

                const newAccessToken = response.data;
                localStorage.setItem('accessToken', newAccessToken);

                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                return api(originalRequest);
            } catch (refreshError) {
                localStorage.removeItem('accessToken');
                window.location.href = '/auth';
                return Promise.reject(refreshError);
            }
        }

        return Promise.reject(error);
    }
);

export default api;