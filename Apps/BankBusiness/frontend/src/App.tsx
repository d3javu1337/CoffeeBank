import React, { useEffect } from 'react';
import { Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import { useAuth } from './hooks/useAuth';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import AuthPage from './pages/AuthPage';
import ProfilePage from './pages/ProfilePage';
import PaymentPage from './pages/PaymentPage';
import ContactPersonPage from './pages/ContactPersonPage';
import AdminPage from './pages/AdminPage';
import AdminClientPage from './pages/AdminClientPage';
import DemoPizzaPage from "./pages/DemoPizzaPage.tsx";

const App: React.FC = () => {
    const { isAuthenticated, isAdmin } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        const handleLogout = () => {
            navigate('/auth');
        };

        window.addEventListener('auth:logout', handleLogout);

        return () => {
            window.removeEventListener('auth:logout', handleLogout);
        };
    }, [navigate]);

    const apiRoute = import.meta.env.VITE_API_URL

    return (
        <Routes>
            <Route element={<Layout />}>
                <Route
                    path="/auth"
                    element={
                        isAuthenticated
                            ? <Navigate to={isAdmin ? "/admin" : "/"} replace />
                            : <AuthPage />
                    }
                />
                <Route
                    path="/"
                    element={
                        <ProtectedRoute>
                            {isAdmin ? <Navigate to="/admin" replace /> : <ProfilePage />}
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/payment"
                    element={
                        <ProtectedRoute>
                            <PaymentPage />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/contacts"
                    element={
                        <ProtectedRoute>
                            <ContactPersonPage />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/admin"
                    element={
                        <ProtectedRoute>
                            <AdminPage />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/admin/client/:clientId"
                    element={
                        <ProtectedRoute>
                            <AdminClientPage />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/pizza"
                    element={
                    <DemoPizzaPage token={localStorage.getItem('demoToken') || ''} apiHost={apiRoute} />
                }
                />
                <Route path="*" element={<Navigate to="/" replace />} />
            </Route>
        </Routes>
    );
};

export default App;