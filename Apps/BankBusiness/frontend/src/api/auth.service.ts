import api from './axios';

interface RegistrationData {
    officialName: string;
    brand: string;
    email: string;
    password: string;
}

interface LoginData {
    email: string;
    password: string;
}

export interface UserInfo {
    email: string;
    role?: string;
}

class AuthService {
    async registration(data: RegistrationData): Promise<void> {
        await api.post('/api/auth/registration', data);
    }

    async login(data: LoginData): Promise<string> {
        const response = await api.post('/api/auth/login', data, {
            headers: {
                'Content-Type': 'application/json',
            },
        });

        const accessToken = response.data;
        localStorage.setItem('accessToken', accessToken);
        return accessToken;
    }

    async refresh(): Promise<string> {
        const response = await api.get('/api/auth/refresh');
        const accessToken = response.data;
        localStorage.setItem('accessToken', accessToken);
        return accessToken;
    }

    logout(): void {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('isAdmin');
    }

    getAccessToken(): string | null {
        return localStorage.getItem('accessToken');
    }

    isAuthenticated(): boolean {
        return !!this.getAccessToken();
    }

    async checkIsAdmin(): Promise<boolean> {
        try {
            const token = localStorage.getItem('accessToken');
            if (!token) return false;

            await api.get('/api/admin');
            localStorage.setItem('isAdmin', 'true');
            return true;
        } catch {
            localStorage.setItem('isAdmin', 'false');
            return false;
        }
    }

    isAdmin(): boolean {
        return localStorage.getItem('isAdmin') === 'true';
    }
}

export default new AuthService();