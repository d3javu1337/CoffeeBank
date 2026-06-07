import api from './axios';

interface Payment {
    paymentId: string;
    amount: number;
}

class PaymentService {
    async getPayments(paymentId?: string): Promise<Payment | Payment[]> {
        const params = paymentId ? { paymentId } : {};
        const response = await api.get<Payment | Payment[]>('/api/payment', { params });
        return response.data;
    }

    async checkPayment(paymentId: string): Promise<boolean> {
        const response = await api.get<boolean>('/api/payment/check', {
            params: { paymentId },
        });
        return response.data === true;
    }
}

export default new PaymentService();