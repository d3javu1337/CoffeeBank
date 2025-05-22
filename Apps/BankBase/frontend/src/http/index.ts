import axios from "axios";


export const api_url = 'http://localhost:8080';

const api = axios.create({
    baseURL: api_url,
    withCredentials: true,
})

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${localStorage.getItem('token')}`;
    }
    return config;
})

api.interceptors.response.use((config) => {
    return config;
}, async (error) => {
    const originalRequest = error.config;
    if (error.response.status === 401 && error.config && !error.config._isRetry) {
        originalRequest._isRetry = true;
        try {
            const response = await axios.get<string>(`${api_url}/auth/refresh`, {withCredentials: true});
            localStorage.setItem('token', response.data);
            return api.request(originalRequest)
        } catch (error) {
            console.log(error);
        }
    }
    throw error;
})

export default api