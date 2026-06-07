import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { AxiosError } from 'axios';
import AdminService from '../api/admin.service';
import type { BusinessClientCompactWithContacts } from '../api/admin.service';

const AdminClientPage: React.FC = () => {
    const { clientId } = useParams<{ clientId: string }>();
    const navigate = useNavigate();

    const [client, setClient] = useState<BusinessClientCompactWithContacts | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        if (clientId) {
            fetchClient(Number(clientId));
        }
    }, [clientId]);

    const fetchClient = async (id: number) => {
        try {
            setIsLoading(true);
            const data = await AdminService.getClientById(id);
            setClient(data);
        } catch (err: unknown) {
            if (err instanceof AxiosError) {
                setError(`Ошибка при загрузке клиента: ${err.response?.data || err.message}`);
            } else if (err instanceof Error) {
                setError(`Ошибка при загрузке клиента: ${err.message}`);
            } else {
                setError('Ошибка при загрузке клиента');
            }
        } finally {
            setIsLoading(false);
        }
    };

    if (isLoading) {
        return <div className="loading">Загрузка...</div>;
    }

    if (!client) {
        return (
            <div className="card">
                <div className="empty-state">
                    <p>Клиент не найден</p>
                    <button onClick={() => navigate('/admin')} className="btn" style={{ marginTop: '15px' }}>
                        Вернуться к списку
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div>
            <button onClick={() => navigate('/admin')} className="btn btn-secondary" style={{ marginBottom: '20px' }}>
                Назад к списку
            </button>

            {error && (
                <div className="error-message">
                    {error}
                    <button onClick={() => setError('')} className="error-close-btn">✕</button>
                </div>
            )}

            <div className="card" style={{ marginBottom: '30px' }}>
                <h1>{client.officialName}</h1>
                <div className="client-info-grid">
                    <div className="client-info-item">
                        <span className="client-info-label">Бренд</span>
                        <span className="client-info-value">{client.brand}</span>
                    </div>
                    <div className="client-info-item">
                        <span className="client-info-label">Email</span>
                        <span className="client-info-value">{client.email}</span>
                    </div>
                </div>
            </div>

            <div className="card">
                <h2>Контактные лица ({client.contacts.length})</h2>
                {client.contacts.length === 0 ? (
                    <div className="empty-state">
                        <p>Нет контактных лиц</p>
                    </div>
                ) : (
                    <div className="contact-grid">
                        {client.contacts.map((contact) => (
                            <div key={contact.id} className="contact-card">
                                <div className="contact-card__header">
                                    <div className="contact-card__avatar">
                                        {contact.surname.charAt(0)}{contact.name.charAt(0)}
                                    </div>
                                    <div className="contact-card__name">
                                        <strong>{contact.surname} {contact.name}</strong>
                                        {contact.patronymic && <div>{contact.patronymic}</div>}
                                    </div>
                                </div>

                                <div className="contact-card__body">
                                    <div className="contact-card__field">
                                        <span className="contact-card__label">Телефон</span>
                                        <span>{contact.phoneNumber}</span>
                                    </div>
                                    <div className="contact-card__field">
                                        <span className="contact-card__label">Email</span>
                                        <span>{contact.email}</span>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default AdminClientPage;