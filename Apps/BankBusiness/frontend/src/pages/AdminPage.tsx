import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AxiosError } from 'axios';
import AdminService from '../api/admin.service';
import type { BusinessClientCompact } from '../api/admin.service';

const AdminPage: React.FC = () => {
    const [clients, setClients] = useState<BusinessClientCompact[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        fetchClients();
    }, []);

    const fetchClients = async () => {
        try {
            setIsLoading(true);
            const data = await AdminService.getClients();
            console.log(data);
            setClients(data);
        } catch (err: unknown) {
            if (err instanceof AxiosError) {
                setError(`Ошибка при загрузке клиентов: ${err.response?.data || err.message}`);
            } else if (err instanceof Error) {
                setError(`Ошибка при загрузке клиентов: ${err.message}`);
            } else {
                setError('Ошибка при загрузке клиентов');
            }
        } finally {
            setIsLoading(false);
        }
    };

    if (isLoading) {
        return <div className="loading">Загрузка...</div>;
    }

    return (
        <div>
            <h1>Админ-панель</h1>
            <p style={{ color: '#666', marginBottom: '20px' }}>Список бизнес-клиентов</p>

            {error && (
                <div className="error-message">
                    {error}
                    <button onClick={() => setError('')} className="error-close-btn">✕</button>
                </div>
            )}

            {clients.length === 0 ? (
                <div className="card">
                    <div className="empty-state">
                        <p>Нет зарегистрированных клиентов</p>
                    </div>
                </div>
            ) : (
                <div className="admin-table-wrapper">
                    <table className="admin-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Официальное название</th>
                            <th>Бренд</th>
                            <th>Email</th>
                            <th>Действия</th>
                        </tr>
                        </thead>
                        <tbody>
                        {clients.map((client) => (
                            <tr key={client.id}>
                                <td>{client.id}</td>
                                <td>{client.officialName}</td>
                                <td>{client.brand}</td>
                                <td>{client.email}</td>
                                <td>
                                    <button
                                        onClick={() => navigate(`/admin/client/${client.id}`)}
                                        className="btn btn-secondary"
                                    >Подробнее
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default AdminPage;