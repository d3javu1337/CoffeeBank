import React, { useEffect, useState } from 'react';
import PaymentService from '../api/payment.service';
import InvoiceService from '../api/invoice.service';

interface Payment {
    paymentId: string;
    amount: number;
}

interface Invoice {
    id: string;
    amount: number;
    providerPaymentAccountId: number;
}

const PaymentPage: React.FC = () => {
    const [payments, setPayments] = useState<Payment[]>([]);
    const [invoices, setInvoices] = useState<Invoice[]>([]);
    const [apiToken, setApiToken] = useState<string>('');
    const [invoiceAmount, setInvoiceAmount] = useState<string>('');
    const [invoiceLink, setInvoiceLink] = useState<string>('');
    const [checkResults, setCheckResults] = useState<Record<string, boolean>>({});
    const [activeTab, setActiveTab] = useState<'payments' | 'invoice' | 'invoices'>('payments');
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    const [checkPaymentId, setCheckPaymentId] = useState('');

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [paymentsData, invoicesData, tokenData] = await Promise.all([
                PaymentService.getPayments(),
                InvoiceService.getInvoices().catch(() => []),
                InvoiceService.getToken().catch(() => null),
            ]);

            setPayments(Array.isArray(paymentsData) ? paymentsData : [paymentsData]);
            setInvoices(invoicesData);
            if (tokenData) {
                setApiToken(tokenData);
                localStorage.setItem("demoToken", tokenData)
            }
        } catch (err) {
            setError(`Ошибка при загрузке данных ${err}`);
        } finally {
            setIsLoading(false);
        }
    };

    const handleCreateInvoice = async () => {
        try {
            setError('');
            const link = await InvoiceService.createInvoice({
                token: apiToken,
                amount: parseFloat(invoiceAmount),
            });
            setInvoiceLink(link);
            setInvoiceAmount('');
            const updatedInvoices = await InvoiceService.getInvoices();
            setInvoices(updatedInvoices);
        } catch (err) {
            setError(`Ошибка при создании счета ${err}`);
        }
    };

    const handleCheckPayment = async (paymentId: string) => {
        try {
            const result = await PaymentService.checkPayment(paymentId);
            setCheckResults(prev => ({ ...prev, [paymentId]: result }));
        } catch (err) {
            setError(`Ошибка при проверке платежа ${err}`);
        }
    };

    const handleCreateToken = async () => {
        try {
            const newToken = await InvoiceService.createToken();
            setApiToken(newToken);
        } catch (err) {
            setError(`Ошибка при создании токена ${err}`);
        }
    };

    if (isLoading) {
        return <div className="loading">Загрузка платежей...</div>;
    }

    return (
        <div>
            {error && (
                <div className="error-message">
                    {error}
                    <button onClick={() => setError('')} className="error-close-btn">✕</button>
                </div>
            )}

            <div className="tabs">
                <button
                    className={`tab ${activeTab === 'payments' ? 'active' : ''}`}
                    onClick={() => setActiveTab('payments')}
                >
                    Платежи
                </button>
                <button
                    className={`tab ${activeTab === 'invoice' ? 'active' : ''}`}
                    onClick={() => setActiveTab('invoice')}
                >
                    Создать счет
                </button>
                <button
                    className={`tab ${activeTab === 'invoices' ? 'active' : ''}`}
                    onClick={() => setActiveTab('invoices')}
                >
                    Список счетов {invoices.length > 0 && `(${invoices.length})`}
                </button>
            </div>

            {activeTab === 'payments' && (
                <div className="card">
                    <h2>Проверить оплату счета</h2><br/>
                    <div style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
                        <div className="form-group" style={{ flex: 1, marginBottom: 0 }}>
                            <input
                                type="text"
                                value={checkPaymentId}
                                onChange={(e) => setCheckPaymentId(e.target.value)}
                                placeholder="Введите Payment ID"
                            />
                        </div>
                        <button
                            onClick={() => {
                                if (checkPaymentId) {
                                    handleCheckPayment(checkPaymentId);
                                }
                            }}
                            className="btn"
                            disabled={!checkPaymentId}
                        >
                            Проверить
                        </button>
                    </div>
                    {checkResults[checkPaymentId] !== undefined && (
                        <div className={`payment-status ${checkResults[checkPaymentId] ? 'payment-status--paid' : 'payment-status--unpaid'}`}>
                            {checkResults[checkPaymentId] ? 'Оплачен' : 'Не оплачен'}
                        </div>
                    )}
                </div>
            )}

            {activeTab === 'invoice' && (
                <div className="card">
                    <h2>Создание счета</h2>

                    <div style={{ marginBottom: '20px' }}>
                        <p><strong>API Токен:</strong> {apiToken || 'Нет токена'}</p>
                        {!apiToken && (
                            <button onClick={handleCreateToken} className="btn" style={{ marginTop: '10px' }}>
                                Создать токен
                            </button>
                        )}
                    </div>

                    <div className="form-group">
                        <label htmlFor="amount">Сумма</label>
                        <input
                            type="number"
                            id="amount"
                            value={invoiceAmount}
                            onChange={(e) => setInvoiceAmount(e.target.value)}
                            step="0.01"
                            min="0.01"
                            placeholder="0.00"
                        />
                    </div>

                    <button
                        onClick={handleCreateInvoice}
                        className="btn"
                        disabled={!apiToken || !invoiceAmount || parseFloat(invoiceAmount) <= 0}
                        style={{ width: '100%' }}
                    >
                        Создать счет
                    </button>

                    {invoiceLink && (
                        <div className="invoice-link" style={{ marginTop: '20px' }}>
                            <p><strong>Счет создан! Ссылка на оплату:</strong></p>
                            <div className="invoice-link-container">
                                <a href={invoiceLink} target="_blank" rel="noopener noreferrer" className="invoice-link">
                                    {invoiceLink}
                                </a>
                                <button
                                    onClick={() => navigator.clipboard.writeText(invoiceLink)}
                                    className="btn btn-secondary"
                                    style={{ whiteSpace: 'nowrap' }}
                                >Копировать
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            )}

            {activeTab === 'invoices' && (
                <div className="card">
                    <h2>Список созданных счетов</h2>
                    {invoices.length === 0 ? (
                        <div className="empty-state">
                            <p>Нет созданных счетов</p>
                            <button
                                onClick={() => setActiveTab('invoice')}
                                className="btn"
                                style={{ marginTop: '15px' }}
                            >Создать первый счет
                            </button>
                        </div>
                    ) : (
                        <div>
                            <div className="invoice-grid">
                                {invoices.map((invoice: Invoice) => (
                                    <div key={invoice.id} className="invoice-card">
                                        <div className="invoice-card__id-label">ID счета</div>
                                        <div className="invoice-card__id-value">{invoice.id}</div>
                                        <div className="invoice-card__body">
                                            <div>
                                                <div className="invoice-card__amount-label">Сумма</div>
                                                <div className="invoice-card__amount-value">
                                                    {invoice.amount.toFixed(2)} руб.
                                                </div>
                                            </div>
                                        </div>
                                        <div className="invoice-card__footer">
                                            <strong>Аккаунт:</strong> {invoice.providerPaymentAccountId}
                                        </div>
                                    </div>
                                ))}
                            </div>
                            <div className="stats-bar">
                                Всего счетов: {invoices.length} |
                                Общая сумма: {invoices.reduce((sum, inv) => sum + inv.amount, 0).toFixed(2)} руб.
                            </div>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default PaymentPage;