import api from './axios';

interface InvoiceRequest {
    token: string;
    amount: number;
}

interface Invoice {
    id: string;
    amount: number;
    providerPaymentAccountId: number;
}

class InvoiceService {
    async createInvoice(data: InvoiceRequest): Promise<string> {
        const response = await api.post<string>('/api/invoice-issue', data);
        return response.data;
    }

    async getInvoices(): Promise<Invoice[]> {
        const response = await api.get<Invoice[]>('/api/invoice');
        return response.data;
    }

    async getToken(): Promise<string> {
        const response = await api.get<string>('/api/token');
        return response.data;
    }

    async createToken(): Promise<string> {
        const response = await api.post<string>('/api/token');
        return response.data;
    }
}

export default new InvoiceService();