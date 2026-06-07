import React, { createContext, useState, useEffect, ReactNode } from 'react';
import AuthService from '../api/auth.service';
import axios from 'axios';

interface AuthContextType {
    isAuthenticated: boolean;
    isLoading: boolean;
    isAdmin: boolean;
    login: (email: string, password: string) => Promise<void>;
    register: (data: any) => Promise<void>;
    logout: () => void;
    checkAuth: () => Promise<void>;
}

export const AuthContext = createContext<AuthContextType>({
    isAuthenticated: false,
    isLoading: true,
    isAdmin: false,
    login: async () => {},
    register: async () => {},
    logout: () => {},
    checkAuth: async () => {},
});

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(
        localStorage.getItem('accessToken') !== null
    );
    const [isAdmin, setIsAdmin] = useState(
        localStorage.getItem('isAdmin') === 'true'
    );
    const [isLoading, setIsLoading] = useState(false);

    const login = async (email: string, password: string) => {
        setIsLoading(true);
        try {
            await AuthService.login({ email, password });
            setIsAuthenticated(true);

            const admin = await AuthService.checkIsAdmin();
            setIsAdmin(admin);
        } catch (e) {
            console.error(e);
            throw e;
        } finally {
            setIsLoading(false);
        }
    };

    const register = async (data: any) => {
        setIsLoading(true);
        try {
            await AuthService.registration(data);
        } catch (e) {
            console.error(e);
            throw e;
        } finally {
            setIsLoading(false);
        }
    };

    const checkAuth = async () => {
        setIsLoading(true);
        try {
            const response = await axios.get<string>('/auth/refresh', {
                withCredentials: true
            });
            localStorage.setItem('accessToken', response.data);
            setIsAuthenticated(true);

            const admin = await AuthService.checkIsAdmin();
            setIsAdmin(admin);
        } catch (e) {
            localStorage.removeItem('accessToken');
            localStorage.removeItem('isAdmin');
            setIsAuthenticated(false);
            setIsAdmin(false);
        } finally {
            setIsLoading(false);
        }
    };

    const logout = () => {
        AuthService.logout();
        setIsAuthenticated(false);
        setIsAdmin(false);
    };

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            setIsAuthenticated(true);
        }
    }, []);

    return (
        <AuthContext.Provider value={{
            isAuthenticated,
            isLoading,
            isAdmin,
            login,
            register,
            logout,
            checkAuth
        }}>
            {children}
        </AuthContext.Provider>
    );
};