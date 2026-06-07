import React, { useEffect, useState } from 'react';
import { AxiosError } from 'axios';
import ContactPersonService from '../api/contact-person.service';
import type { ContactPersonRead, ContactPersonCreate, ContactPersonUpdate } from '../api/contact-person.service';

const emptyForm: ContactPersonCreate & { id?: number } = {
    surname: '',
    name: '',
    patronymic: '',
    phoneNumber: '',
    email: '',
};

const ContactPersonPage: React.FC = () => {
    const [persons, setPersons] = useState<ContactPersonRead[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [editingId, setEditingId] = useState<number | null>(null);
    const [formData, setFormData] = useState(emptyForm);

    const fetchPersons = async () => {
        try {
            setIsLoading(true);
            const data = await ContactPersonService.getAll();
            setPersons(data);
        } catch (err: unknown) {
            if (err instanceof AxiosError) {
                setError(`Ошибка при загрузке контактных лиц: ${err.response?.data || err.message}`);
            } else if (err instanceof Error) {
                setError(`Ошибка при загрузке контактных лиц: ${err.message}`);
            } else {
                setError('Ошибка при загрузке контактных лиц');
            }
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchPersons();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        try {
            if (editingId !== null) {
                const updateData: ContactPersonUpdate = {
                    id: editingId,
                    ...formData,
                };
                await ContactPersonService.update(updateData);
                setSuccess('Контактное лицо обновлено');
            } else {
                await ContactPersonService.create(formData);
                setSuccess('Контактное лицо создано');
            }

            resetForm();
            await fetchPersons();
        } catch (err: unknown) {
            if (err instanceof AxiosError) {
                setError(`Ошибка при сохранении: ${err.response?.data || err.message}`);
            } else if (err instanceof Error) {
                setError(`Ошибка при сохранении: ${err.message}`);
            } else {
                setError('Ошибка при сохранении');
            }
        }
    };

    const handleEdit = (person: ContactPersonRead) => {
        setFormData({
            surname: person.surname,
            name: person.name,
            patronymic: person.patronymic,
            phoneNumber: person.phoneNumber,
            email: person.email,
            id: person.id,
        });
        setEditingId(person.id);
        setShowForm(true);
    };

    const handleDelete = async (id: number) => {
        if (!window.confirm('Удалить контактное лицо?')) return;

        try {
            setError('');
            await ContactPersonService.delete(id);
            setSuccess('Контактное лицо удалено');
            await fetchPersons();
        } catch (err: unknown) {
            if (err instanceof AxiosError) {
                setError(`Ошибка при удалении: ${err.response?.data || err.message}`);
            } else if (err instanceof Error) {
                setError(`Ошибка при удалении: ${err.message}`);
            } else {
                setError('Ошибка при удалении');
            }
        }
    };

    const resetForm = () => {
        setFormData(emptyForm);
        setEditingId(null);
        setShowForm(false);
    };

    if (isLoading) {
        return <div className="loading">Загрузка...</div>;
    }

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                <h1>Контактные лица</h1>
                <button
                    onClick={() => {
                        resetForm();
                        setShowForm(!showForm);
                    }}
                    className="btn"
                >
                    {showForm ? 'Отмена' : 'Добавить контактное лицо'}
                </button>
            </div>

            {error && (
                <div className="error-message">
                    {error}
                    <button onClick={() => setError('')} className="error-close-btn">✕</button>
                </div>
            )}

            {success && (
                <div className="success-message">
                    {success}
                    <button onClick={() => setSuccess('')} className="error-close-btn">✕</button>
                </div>
            )}

            {showForm && (
                <div className="card" style={{ marginBottom: '30px' }}>
                    <h2>{editingId !== null ? 'Редактировать' : 'Новое'} контактное лицо</h2>
                    <form onSubmit={handleSubmit}>
                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px' }}>
                            <div className="form-group">
                                <label htmlFor="surname">Фамилия *</label>
                                <input
                                    type="text"
                                    id="surname"
                                    name="surname"
                                    value={formData.surname}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="name">Имя *</label>
                                <input
                                    type="text"
                                    id="name"
                                    name="name"
                                    value={formData.name}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="patronymic">Отчество</label>
                                <input
                                    type="text"
                                    id="patronymic"
                                    name="patronymic"
                                    value={formData.patronymic}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="phoneNumber">Телефон *</label>
                                <input
                                    type="tel"
                                    id="phoneNumber"
                                    name="phoneNumber"
                                    value={formData.phoneNumber}
                                    onChange={handleChange}
                                    required
                                    placeholder="+7 (999) 123-45-67"
                                />
                            </div>
                            <div className="form-group" style={{ gridColumn: '1 / -1' }}>
                                <label htmlFor="email">Email *</label>
                                <input
                                    type="email"
                                    id="email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                        </div>

                        <div style={{ display: 'flex', gap: '10px', marginTop: '20px' }}>
                            <button type="submit" className="btn">
                                {editingId !== null ? 'Сохранить' : 'Создать'}
                            </button>
                            <button type="button" onClick={resetForm} className="btn btn-secondary">
                                Отмена
                            </button>
                        </div>
                    </form>
                </div>
            )}

            {persons.length === 0 ? (
                <div className="card">
                    <div className="empty-state">
                        <p>Нет контактных лиц</p>
                        <button
                            onClick={() => setShowForm(true)}
                            className="btn"
                            style={{ marginTop: '15px' }}
                        >Добавить первое контактное лицо
                        </button>
                    </div>
                </div>
            ) : (
                <div className="contact-grid">
                    {persons.map((person) => (
                        <div key={person.id} className="contact-card">
                            <div className="contact-card__header">
                                <div className="contact-card__avatar">
                                    {person.surname.charAt(0)}{person.name.charAt(0)}
                                </div>
                                <div className="contact-card__name">
                                    <strong>{person.surname} {person.name}</strong>
                                    {person.patronymic && <div>{person.patronymic}</div>}
                                </div>
                            </div>

                            <div className="contact-card__body">
                                <div className="contact-card__field">
                                    <span className="contact-card__label">Телефон</span>
                                    <span>{person.phoneNumber}</span>
                                </div>
                                <div className="contact-card__field">
                                    <span className="contact-card__label">Email</span>
                                    <span>{person.email}</span>
                                </div>
                            </div>

                            <div className="contact-card__actions">
                                <button
                                    onClick={() => handleEdit(person)}
                                    className="btn btn-secondary"
                                >Редактировать
                                </button>
                                <button
                                    onClick={() => handleDelete(person.id)}
                                    className="btn btn-danger"
                                >Удалить
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ContactPersonPage;