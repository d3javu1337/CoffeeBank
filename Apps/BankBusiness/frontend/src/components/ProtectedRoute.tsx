import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const { isAuthenticated, isLoading } = useAuth();

    if (isLoading) {
        return <div className="loading">Загрузка...</div>;
    }

    if (!isAuthenticated) {
        return <Navigate to="/auth" replace />;
    }

    return <>{children}</>;
};

export default ProtectedRoute;