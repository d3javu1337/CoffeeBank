import React, { useState } from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import {AxiosError} from "axios";

const AuthPage: React.FC = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [formData, setFormData] = useState({
        officialName: '',
        brand: '',
        email: '',
        password: '',
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const { login, register } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const from = (location.state as { from?: { pathname: string } })?.from?.pathname || '/';

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        setIsLoading(true);

        try {
            if (isLogin) {
                await login(formData.email, formData.password);
                const admin = localStorage.getItem('isAdmin') === 'true';
                navigate(admin ? '/admin' : from, { replace: true });
            } else {
                await register({
                    officialName: formData.officialName,
                    brand: formData.brand,
                    email: formData.email,
                    password: formData.password,
                });
                setSuccess('Регистрация прошла успешно! Теперь вы можете войти.');
                setIsLogin(true);
            }
        } catch (err: unknown) {
            if (err instanceof AxiosError) {
                setError(err.response?.data?.message || 'Произошла ошибка');
            } else {
                setError('Произошла ошибка');
            }
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            minHeight: '80vh'
        }}>
            <div className="card" style={{ maxWidth: '450px', width: '100%' }}>
                <div className="tabs">
                    <button
                        className={`tab ${isLogin ? 'active' : ''}`}
                        onClick={() => setIsLogin(true)}
                    >
                        Вход
                    </button>
                    <button
                        className={`tab ${!isLogin ? 'active' : ''}`}
                        onClick={() => setIsLogin(false)}
                    >
                        Регистрация
                    </button>
                </div>

                {error && <div className="error-message">{error}</div>}
                {success && <div className="success-message">{success}</div>}

                <form onSubmit={handleSubmit}>
                    {!isLogin && (
                        <>
                            <div className="form-group">
                                <label htmlFor="officialName">Официальное название</label>
                                <input
                                    type="text"
                                    id="officialName"
                                    name="officialName"
                                    value={formData.officialName}
                                    onChange={handleChange}
                                    required={!isLogin}
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="brand">Бренд</label>
                                <input
                                    type="text"
                                    id="brand"
                                    name="brand"
                                    value={formData.brand}
                                    onChange={handleChange}
                                    required={!isLogin}
                                />
                            </div>
                        </>
                    )}

                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Пароль</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            minLength={6}
                        />
                    </div>

                    <button type="submit" className="btn" disabled={isLoading} style={{ width: '100%' }}>
                        {isLoading ? 'Загрузка...' : isLogin ? 'Войти' : 'Зарегистрироваться'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default AuthPage;