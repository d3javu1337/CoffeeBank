import React, { useEffect, useState } from 'react';
import BusinessClientService from '../api/business-client.service';
import AccountService from '../api/account.service';

const ProfilePage: React.FC = () => {
    const [clientInfo, setClientInfo] = useState<any>(null);
    const [account, setAccount] = useState<any>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [clientData, accountData] = await Promise.all([
                    BusinessClientService.getClientInfo(),
                    AccountService.getAccount(),
                ]);
                setClientInfo(clientData);
                setAccount(accountData);
            } catch (err: any) {
                setError('Ошибка при загрузке данных');
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, []);

    const handleCreateAccount = async () => {
        try {
            await AccountService.createAccount();
            const accountData = await AccountService.getAccount();
            setAccount(accountData);
        } catch (err: any) {
            setError('Ошибка при создании аккаунта');
        }
    };

    if (isLoading) {
        return <div className="loading">Загрузка профиля...</div>;
    }

    return (
        <div>
            {error && <div className="error-message">{error}</div>}

            {clientInfo && (
                <div className="card">
                    <h2>Информация о компании</h2>
                    <p><strong>Официальное название:</strong> {clientInfo.officialName}</p>
                    <p><strong>Бренд:</strong> {clientInfo.brand}</p>
                </div>
            )}

            {account ? (
                <div className="card">
                    <h2>Платежный аккаунт</h2>
                    <p><strong>ID:</strong> {account.id}</p>
                    <p><strong>Имя:</strong> {account.name}</p>
                    <p><strong>Депозит:</strong> ${account.deposit.toFixed(2)}</p>
                </div>
            ) : (
                <div className="card">
                    <h2>Платежный аккаунт</h2>
                    <p>У вас еще нет платежного аккаунта</p>
                    <button onClick={handleCreateAccount} className="btn">
                        Создать аккаунт
                    </button>
                </div>
            )}
        </div>
    );
};

export default ProfilePage;