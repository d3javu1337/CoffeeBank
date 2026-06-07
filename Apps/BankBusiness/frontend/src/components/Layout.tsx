import React from 'react';
import { Link, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const Layout: React.FC = () => {
    const { isAuthenticated, isAdmin, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/auth');
    };

    return (
        <>
            <nav className="navbar">
                <Link to={isAdmin ? '/admin' : '/'} className="navbar-brand">
                    BusinessClient
                </Link>
                {isAuthenticated && (
                    <ul className="navbar-nav">
                        {!isAdmin && (
                            <>
                                <li>
                                    <Link to="/">Профиль</Link>
                                </li>
                                <li>
                                    <Link to="/payment">Платежи</Link>
                                </li>
                                <li>
                                    <Link to="/contacts">Контакты</Link>
                                </li>
                            </>
                        )}
                        {isAdmin && (
                            <li>
                                <Link to="/admin">Админ-панель</Link>
                            </li>
                        )}
                        <li>
                            <button onClick={handleLogout} className="btn btn-secondary">
                                Выйти
                            </button>
                        </li>
                    </ul>
                )}
            </nav>
            <div className="container">
                <Outlet />
            </div>
        </>
    );
};

export default Layout;